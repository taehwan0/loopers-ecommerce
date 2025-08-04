package com.loopers.application.order;

import com.loopers.domain.order.CreateOrderItemDTO;
import com.loopers.domain.order.OrderEntity;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.payment.PaymentEntity;
import com.loopers.domain.payment.PaymentMethod;
import com.loopers.domain.payment.PaymentService;
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
	private final PaymentService paymentService;

	@Transactional
	public OrderInfo createOrder(CreateOrderCommand command) {
		if (command.items().isEmpty()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문 항목은 필수 입력값 입니다.");
		}

		UserEntity user = userService.findByLoginId(command.loginId())
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[loginId = " + command.loginId() + "] 사용자를 찾을 수 없습니다."));

		// 이미 생성된 주문의 경우, idempotencyKey를 통해 조회하여 반환
		Optional<OrderEntity> orderOptional = orderService.getOrder(command.idempotencyKey());
		if (orderOptional.isPresent()) {
			OrderEntity order = orderOptional.get();
			return OrderInfo.from(order, order.getOrderItems());
		}

		List<CreateOrderItemDTO> itemDtos = command.items()
				.stream()
				.map(i -> {
							if (productService.getProduct(i.productId()).isEmpty()) {
								throw new CoreException(ErrorType.NOT_FOUND, "[productId = " + i.productId() + "] 상품을 찾을 수 없습니다.");
							}
							return CreateOrderItemDTO.of(i.productId(), i.quantity());
						}
				)
				.toList();

		OrderEntity order = orderService.createOrder(command.idempotencyKey(), user.getId(), itemDtos);

		return OrderInfo.from(order, order.getOrderItems());
	}

	@Transactional
	public PaymentInfo payOrderByPoint(String loginId, Long orderId) {
		UserEntity user = userService.findByLoginId(loginId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[loginId = " + loginId + "] 사용자를 찾을 수 없습니다."));

		OrderEntity order = orderService.getOrder(orderId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[orderId = " + orderId + "] 주문을 찾을 수 없습니다."));

		if (order.getOrderStatus() != OrderStatus.PENDING) {
			throw new CoreException(ErrorType.CONFLICT, "주문 상태가 결제 대기중이 아닙니다.");
		}

		Long totalAmount = order.getOrderItems().stream()
				.map(item -> {
					ProductEntity product = productService.getProduct(item.getProductId())
							.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[productId = " + item.getProductId() + "] 상품을 찾을 수 없습니다."));

					product.decreaseStock(item.getQuantity());

					return product.getPrice().getAmount() * item.getQuantity();
				})
				.reduce(0L, Long::sum);

		user.debitPoints(totalAmount);

		PaymentEntity payment = paymentService.save(orderId, PaymentMethod.POINT, totalAmount);

		order.updateStatus(OrderStatus.PAYMENT_CONFIRMED);

		return PaymentInfo.from(payment, order);
	}
}
