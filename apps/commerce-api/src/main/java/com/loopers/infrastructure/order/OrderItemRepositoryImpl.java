package com.loopers.infrastructure.order;

import com.loopers.domain.order.OrderItemEntity;
import com.loopers.domain.order.OrderItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderItemRepositoryImpl implements OrderItemRepository {

	private final OrderItemJpaRepository orderItemJpaRepository;

	@Override
	public List<OrderItemEntity> saveAll(List<OrderItemEntity> orderItems) {
		return orderItemJpaRepository.saveAll(orderItems);
	}
}
