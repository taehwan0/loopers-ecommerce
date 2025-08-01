package com.loopers.domain.order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;

	public Optional<OrderEntity> getOrder(UUID idempotencyKey) {
		return orderRepository.findByIdempotencyKey(idempotencyKey);
	}

	public OrderEntity createOrder(UUID idempotencyKey, Long userId, List<CreateOrderItemDTO> itemDtos) {
		OrderEntity order = orderRepository.save(OrderEntity.of(idempotencyKey, userId));

		List<OrderItemEntity> itemEntities = itemDtos.stream()
				.map(item -> OrderItemEntity.of(
								order,
								item.productId(),
								item.quantity()
						)
				)
				.toList();

		orderItemRepository.saveAll(itemEntities);

		order.addItems(itemEntities);

		return order;
	}
}
