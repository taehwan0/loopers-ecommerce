package com.loopers.domain.order;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderService {

	private final OrderRepository orderRepository;

	public OrderEntity getOrder(Long id) {
		return orderRepository.findById(id)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[id = " + id + "] 주문을 찾을 수 없습니다."));
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
