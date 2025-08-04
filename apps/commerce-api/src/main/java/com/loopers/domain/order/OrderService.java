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

	public Optional<OrderEntity> getOrder(Long id) {
		return orderRepository.findById(id);
	}

	public Optional<OrderEntity> getOrder(UUID idempotencyKey) {
		return orderRepository.findByIdempotencyKey(idempotencyKey);
	}

	public OrderEntity createOrder(UUID idempotencyKey, Long userId, List<CreateOrderItemDTO> itemDtos) {
		OrderEntity order = OrderEntity.of(idempotencyKey, userId);

		List<OrderItemEntity> itemEntities = itemDtos.stream()
				.map(item -> OrderItemEntity.of(
								order,
								item.productId(),
								item.price(),
								item.quantity()
						)
				)
				.toList();

		order.addItems(itemEntities);

		return orderRepository.save(order);
	}
}
