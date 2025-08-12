package com.loopers.application.order;

import com.loopers.domain.coupon.CouponDiscountCalculator;
import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.coupon.UserCouponEntity;
import com.loopers.domain.order.OrderEntity;
import com.loopers.domain.order.OrderItemCriteria;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointService;
import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
import com.loopers.domain.vo.Price;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
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

	// 주문 생성 자체가 실패하는 경우엔 400에러로 반환할 수 있도록 한다.
	// 주문 생성 자체는 성공했지만,
	@Transactional
	public OrderInfo placeOrder(PlaceOrderCommand command) {
		// 이미 멱등키로 생성한 주문이 존재한다면, 그대로 반환한다.
		Optional<OrderEntity> orderOptional = orderService.getOrder(command.idempotencyKey());
		if (orderOptional.isPresent()) {
			OrderEntity order = orderOptional.get();
			return OrderInfo.from(order);
		}

		if (command.items().isEmpty()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문 항목은 필수 입력값 입니다.");
		}

		UserEntity user = userService.findByLoginId(command.loginId())
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[loginId = " + command.loginId() + "] 사용자를 찾을 수 없습니다."));

		List<OrderItemCriteria> orderItemCriteria = command.items().stream()
				.map(i -> {
					ProductEntity product = productService.getProduct(i.productId());
					return OrderItemCriteria.of(product.getId(), product.getPrice(), i.quantity());
				})
				.toList();

		OrderEntity order = orderService.createOrder(
				command.idempotencyKey(),
				user.getId(),
				command.couponId(),
				orderItemCriteria
		);

		Price totalPrice = order.getTotalPrice();

		Price discountedPrice = Optional.ofNullable(command.couponId())
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

		order.getOrderItems().forEach(item -> productService.decreaseStock(item.getProductId(), item.getQuantity()));

		pointService.deductPoint(user.getId(), Point.of(discountedPrice.getAmount()));
		paymentService.save(order.getId(), PaymentMethod.POINT, discountedPrice.getAmount());

		order.paymentConfirm();

		return OrderInfo.from(order);
	}
}
