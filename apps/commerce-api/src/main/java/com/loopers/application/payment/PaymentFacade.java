package com.loopers.application.payment;

import com.loopers.application.order.CardPaymentCommand;
import com.loopers.application.order.PaymentInfo;
import com.loopers.application.order.PointPaymentCommand;
import com.loopers.application.payment.PaymentCallbackCommand.TransactionStatus;
import com.loopers.domain.coupon.CouponDiscountCalculator;
import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.coupon.UserCouponEntity;
import com.loopers.domain.order.OrderEntity;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.payment.PaymentAdaptor;
import com.loopers.domain.payment.PaymentAdaptor.PaymentRequest;
import com.loopers.domain.payment.PaymentAdaptor.PaymentRequest.CardNumber;
import com.loopers.domain.payment.PaymentAdaptor.PaymentRequest.CardType;
import com.loopers.domain.payment.PaymentAdaptor.PaymentResponse;
import com.loopers.domain.payment.PaymentEntity;
import com.loopers.domain.payment.PaymentEvent;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointAccountEntity;
import com.loopers.domain.point.PointService;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.push.PushService;
import com.loopers.domain.user.UserService;
import com.loopers.domain.vo.Price;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentFacade {

	public static final String ORDER_PREFIX = "LOOPERS_";

	private final PaymentService paymentService;
	private final OrderService orderService;
	private final PointService pointService;
	private final PaymentAdaptor paymentAdaptor;
	private final CouponService couponService;
	private final CouponDiscountCalculator couponDiscountCalculator;
	private final ApplicationEventPublisher eventPublisher;
	private final ProductService productService;
	private final PushService pushService;
	private final UserService userService;

	@Transactional
	public PaymentInfo paymentByPoint(PointPaymentCommand command) {
		OrderEntity order = validateAndGetOrder(command.orderId());

		Price totalPrice = calculateTotalPrice(order);
		PaymentEntity payment = paymentService.save(order.getId(), PaymentMethod.POINT, totalPrice.getAmount());

		PointAccountEntity pointAccount = pointService.getPointAccount(order.getUserId());
		if (pointAccount.getPointBalance().getValue().compareTo(BigDecimal.valueOf(totalPrice.getAmount())) <= 0) {
			payment.fail();
			order.paymentFailed();

			eventPublisher.publishEvent(PaymentEvent.PaymentFail.of(payment.getOrderId(), payment.getId()));
		} else {
			pointService.deductPoint(order.getUserId(), Point.of(totalPrice.getAmount()));
			payment.success();
			order.paymentConfirm();

			eventPublisher.publishEvent(PaymentEvent.PaymentSuccess.of(payment.getOrderId(), payment.getId()));
		}

		return PaymentInfo.from(payment);
	}

	@Transactional
	public PaymentInfo paymentByCard(CardPaymentCommand command) {
		OrderEntity order = validateAndGetOrder(command.orderId());

		Price totalPrice = calculateTotalPrice(order);
		PaymentEntity payment = paymentService.findPaymentByOrderId(order.getId())
				.orElse(paymentService.save(order.getId(), PaymentMethod.CARD, totalPrice.getAmount()));

		if (payment.getTransactionKey() != null) {
			throw new CoreException(ErrorType.CONFLICT, "결제 처리중인 주문입니다.");
		}

		var request = PaymentRequest.of(
				ORDER_PREFIX + order.getId(),
				CardType.of(command.cardType()),
				CardNumber.of(command.cardNumber()),
				totalPrice.getAmount()
		);

		PaymentResponse paymentResponse = paymentAdaptor.requestPayment(request);
		payment.setTransactionKey(paymentResponse.transactionKey());

		return PaymentInfo.from(payment);
	}

	private OrderEntity validateAndGetOrder(Long orderId) {
		OrderEntity order = orderService.getOrder(orderId);

		if (order.getOrderStatus() != OrderStatus.PENDING) {
			throw new CoreException(ErrorType.CONFLICT, "결제 대기중인 주문이 아닙니다.");
		}

		return order;
	}

	private Price calculateTotalPrice(OrderEntity order) {
		Price totalPrice = order.getTotalPrice();

		return Optional.ofNullable(order.getCouponId())
				.map(couponId -> {
					UserCouponEntity coupon = couponService.getUserCouponById(couponId);

					if (coupon.isUsed()) {
						throw new CoreException(ErrorType.CONFLICT, "이미 사용된 쿠폰입니다.");
					}

					return couponDiscountCalculator.calculateDiscount(totalPrice, coupon.getCouponPolicy());
				})
				.orElse(totalPrice);
	}

	public void handlePaymentCallback(PaymentCallbackCommand command) {
		// command 들어온 결과 확인하기, pending인 경우에는 아무런 처리도 하지 않는다. 추후 scheduler를 통해서 처리한다.
		TransactionStatus status = command.status();
		PaymentEntity payment = paymentService.getByTransactionKey(command.transactionKey());
		// PG 조회를 통한 유효성 검증 필요!

		if (status == TransactionStatus.SUCCESS) {
			eventPublisher.publishEvent(PaymentEvent.PaymentSuccess.of(payment.getOrderId(), payment.getId()));
		}

		if (status == TransactionStatus.FAIL) {
			eventPublisher.publishEvent(PaymentEvent.PaymentFail.of(payment.getOrderId(), payment.getId()));
		}
	}

	@Transactional
	public void paymentSuccess(PaymentEvent.PaymentSuccess event) {
		OrderEntity order = orderService.getOrder(event.orderId());
		order.paymentConfirm();

		PaymentEntity payment = paymentService.getById(event.paymentId());
		payment.success();
	}

	@Transactional
	public void paymentFail(PaymentEvent.PaymentFail event) {
		OrderEntity order = orderService.getOrder(event.orderId());
		order.paymentFailed();

		PaymentEntity payment = paymentService.getById(event.paymentId());
		payment.fail();

		order.getOrderItems().forEach(item -> productService.increaseStock(item.getProductId(), item.getQuantity()));
	}
}
