package com.loopers.interfaces.api.order;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.order.OrderV1Dto.PlaceOrderResponse;
import io.swagger.v3.oas.annotations.Operation;

public interface OrderV1ApiSpec {
	@Operation(
			summary = "주문 생성",
			description = "주문을 생성합니다."
	)
	ApiResponse<PlaceOrderResponse> placeOrder(String loginId, OrderV1Dto.PlaceOrderRequest request);
}
