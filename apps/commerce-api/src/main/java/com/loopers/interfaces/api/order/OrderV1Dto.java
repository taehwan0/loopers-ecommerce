package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderInfo;
import java.util.List;

public class OrderV1Dto {

	public record PlaceOrderRequest(
			String idempotencyKey,
			List<OrderItem> items,
			Long couponId
	) {

		public record OrderItem(
				Long productId,
				int quantity
		) {

		}
	}

	public record PlaceOrderResponse(
			Long orderId,
			String orderStatus
	) {

		public static PlaceOrderResponse from(OrderInfo orderInfo) {
			return new PlaceOrderResponse(orderInfo.id(), orderInfo.orderStatus());
		}
	}
}
