package com.loopers.application.order;

import com.loopers.domain.coupon.CouponDiscountCalculator;
import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.coupon.UserCouponEntity;
import com.loopers.domain.order.OrderEntity;
import com.loopers.domain.order.OrderItemCriteria;
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
import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
import com.loopers.domain.vo.Price;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderFacade {

	private final OrderService orderService;
	private final ProductService productService;
	private final UserService userService;
	private final PaymentService paymentService;
	private final PointService pointService;
	private final CouponService couponService;
	private final CouponDiscountCalculator couponDiscountCalculator;
	private final PaymentClient paymentClient;


	@Transactional
	public OrderInfo placeOrder(PlaceOrderCommand command) {
		Optional<OrderEntity> orderOptional = orderService.getOrder(command.idempotencyKey());
		if (orderOptional.isPresent()) {
			OrderEntity order = orderOptional.get();
			return OrderInfo.from(order);
		}

		if (command.items().isEmpty()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문 항목은 필수 입력값 입니다.");
		}

		UserEntity user = userService.findByLoginId(command.loginId())
				.orElseThrow(
						() -> new CoreException(ErrorType.NOT_FOUND, "[loginId = " + command.loginId() + "] 사용자를 찾을 수 없습니다."));

		List<OrderItemCriteria> orderItemCriteria = command.items().stream()
				.map(i -> {
					ProductEntity product = productService.getProduct(i.productId());
					return OrderItemCriteria.of(product.getId(), product.getPrice(), i.quantity());
				})
				.toList();

		if (command.couponId() != null) {
			couponService.getUserCouponById(command.couponId());
		}

		OrderEntity order = orderService.createOrder(
				command.idempotencyKey(),
				user.getId(),
				command.couponId(),
				orderItemCriteria
		);

		order.getOrderItems().forEach(item -> productService.decreaseStock(item.getProductId(), item.getQuantity()));

		return OrderInfo.from(order);
	}

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
