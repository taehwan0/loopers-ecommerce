package com.loopers.domain.order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

	Optional<OrderEntity> findByIdempotencyKey(UUID idempotencyKey);

	OrderEntity save(OrderEntity order);
}
