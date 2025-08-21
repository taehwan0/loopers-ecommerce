package com.loopers.application.payment;

import com.loopers.application.order.CardPaymentCommand;
import com.loopers.application.order.PaymentInfo;
import com.loopers.application.order.PointPaymentCommand;
import com.loopers.domain.coupon.CouponDiscountCalculator;
import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.coupon.UserCouponEntity;
import com.loopers.domain.order.OrderEntity;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.payment.PaymentClient;
import com.loopers.domain.payment.PaymentClient.PaymentRequest;
import com.loopers.domain.payment.PaymentClient.PaymentRequest.CardNumber;
import com.loopers.domain.payment.PaymentClient.PaymentRequest.CardType;
import com.loopers.domain.payment.PaymentClient.PaymentResponse;
import com.loopers.domain.payment.PaymentEntity;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointAccountEntity;
import com.loopers.domain.point.PointService;
import com.loopers.domain.vo.Price;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentFacade {

	private final PaymentService paymentService;
	private final OrderService orderService;
	private final PointService pointService;
	private final PaymentClient paymentClient;
	private final CouponService couponService;
	private final CouponDiscountCalculator couponDiscountCalculator;

	@Transactional
	public PaymentInfo paymentByPoint(PointPaymentCommand command) {
		OrderEntity order = validateAndGetOrder(command.orderId());

		Price totalPrice = calculateTotalPrice(order);
		PaymentEntity payment = paymentService.save(order.getId(), PaymentMethod.POINT, totalPrice.getAmount());

		PointAccountEntity pointAccount = pointService.getPointAccount(order.getUserId());
		if (pointAccount.getPointBalance().getValue().compareTo(BigDecimal.valueOf(totalPrice.getAmount())) <= 0) {
			payment.fail();
			order.paymentFailed();
		} else {
			pointService.deductPoint(order.getUserId(), Point.of(totalPrice.getAmount()));
			payment.success();
			order.paymentConfirm();
		}

		return PaymentInfo.from(payment);
	}

	@Transactional
	public PaymentInfo paymentByCard(CardPaymentCommand command) {
		OrderEntity order = validateAndGetOrder(command.orderId());

		Price totalPrice = calculateTotalPrice(order);
		PaymentEntity payment = paymentService.save(order.getId(), PaymentMethod.CARD, totalPrice.getAmount());

		var request = PaymentRequest.of(
				String.valueOf(order.getId()),
				CardType.of(command.cardType()),
				CardNumber.of(command.cardNumber()),
				totalPrice.getAmount()
		);
		PaymentResponse paymentResponse = paymentClient.requestPayment(request);

		payment.setTransactionKey(paymentResponse.transactionKey());

		return PaymentInfo.from(payment);
	}

	private OrderEntity validateAndGetOrder(Long orderId) {
		OrderEntity order = orderService.getOrder(orderId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[orderId = " + orderId + "] 주문을 찾을 수 없습니다."));

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

					Price price = couponDiscountCalculator.calculateDiscount(totalPrice, coupon.getCouponPolicy());
					coupon.use();

					return price;
				})
				.orElse(totalPrice);
	}
}
