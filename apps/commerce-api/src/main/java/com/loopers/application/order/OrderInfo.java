package com.loopers.application.order;

import com.loopers.domain.order.OrderEntity;
import com.loopers.domain.order.OrderItemEntity;
import java.util.List;

public record OrderInfo(
		Long id,
		String orderStatus,
		List<OrderItemInfo> orderItems
) {

	public static OrderInfo from(OrderEntity order, List<OrderItemEntity> orderItems) {
		List<OrderItemInfo> orderItemInfos = orderItems.stream()
				.map(OrderItemInfo::from)
				.toList();

		return new OrderInfo(order.getId(), order.getOrderStatus().name(), orderItemInfos);
	}

	public record OrderItemInfo(
			Long id,
			Long productId,
			int quantity
	) {

		public static OrderItemInfo from(OrderItemEntity orderItem) {
			return new OrderItemInfo(
					orderItem.getId(),
					orderItem.getProductId(),
					orderItem.getQuantity()
			);
		}
	}
}
