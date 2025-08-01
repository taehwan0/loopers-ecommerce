package com.loopers.infrastructure.order;

import com.loopers.domain.order.OrderEntity;
import com.loopers.domain.order.OrderRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImp implements OrderRepository {

	private final OrderJpaRepository orderJpaRepository;

	@Override
	public Optional<OrderEntity> findByIdempotencyKey(UUID idempotencyKey) {
		return orderJpaRepository.findByIdempotencyKey(idempotencyKey);
	}

	@Override
	public OrderEntity save(OrderEntity order) {
		return orderJpaRepository.save(order);
	}
}
