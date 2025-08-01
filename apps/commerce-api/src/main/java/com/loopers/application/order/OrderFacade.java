package com.loopers.application.order;

import com.loopers.domain.order.CreateOrderItemDTO;
import com.loopers.domain.order.OrderEntity;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.product.ProductService;
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

	@Transactional
	public OrderInfo createOrder(CreateOrderCommand command) {
		if (command.items().isEmpty()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문 항목은 필수 입력값 입니다.");
		}

		if (userService.getUser(command.userId()).isEmpty()) {
			throw new CoreException(ErrorType.NOT_FOUND, "[userId = " + command.userId() + "] 사용자를 찾을 수 없습니다.");
		}

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

		OrderEntity order = orderService.createOrder(command.idempotencyKey(), command.userId(), itemDtos);

		return OrderInfo.from(order, order.getOrderItems());
	}
}
