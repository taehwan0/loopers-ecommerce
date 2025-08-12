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

	public OrderEntity createOrder(UUID idempotencyKey, Long userId, Long couponId, List<OrderItemCriteria> criteria) {
		OrderEntity order = OrderEntity.of(idempotencyKey, userId, couponId);

		List<OrderItemEntity> itemEntities = criteria.stream()
				.map(c -> OrderItemEntity.of(
								order,
								c.productId(),
								c.price(),
								c.quantity()
						)
				)
				.toList();

		order.addItems(itemEntities);

		return orderRepository.save(order);
	}
}
