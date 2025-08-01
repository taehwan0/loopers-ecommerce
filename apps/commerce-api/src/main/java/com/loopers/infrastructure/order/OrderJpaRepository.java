package com.loopers.infrastructure.order;

import com.loopers.domain.order.OrderEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
	Optional<OrderEntity> findByIdempotencyKey(UUID idempotencyKey);
}
