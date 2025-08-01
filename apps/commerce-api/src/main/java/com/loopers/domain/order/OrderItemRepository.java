package com.loopers.domain.order;

import java.util.List;

public interface OrderItemRepository {

	List<OrderItemEntity> saveAll(List<OrderItemEntity> orderItems);
}
