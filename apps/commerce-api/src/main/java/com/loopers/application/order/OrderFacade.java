package com.loopers.application.order;

import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.order.OrderEntity;
import com.loopers.domain.order.OrderItemCriteria;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
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
	private final CouponService couponService;

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
}
