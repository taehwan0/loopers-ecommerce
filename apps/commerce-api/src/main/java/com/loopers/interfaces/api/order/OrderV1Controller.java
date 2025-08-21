package com.loopers.interfaces.api.order;


import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.OrderInfo;
import com.loopers.application.order.PlaceOrderCommand;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.order.OrderV1Dto.PlaceOrderRequest;
import com.loopers.interfaces.api.order.OrderV1Dto.PlaceOrderResponse;
import com.loopers.support.RequireUserLoginId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@RestController
public class OrderV1Controller implements OrderV1ApiSpec {

	private static final String X_USER_ID_HEADER = "X-USER-ID";

	private final OrderFacade orderFacade;

	@RequireUserLoginId
	@PostMapping("")
	@Override
	public ApiResponse<PlaceOrderResponse> placeOrder(
			@RequestHeader(X_USER_ID_HEADER) String loginId,
			@RequestBody PlaceOrderRequest request
	) {
		var command = PlaceOrderCommand.of(
				UUID.fromString(request.idempotencyKey()),
				loginId,
				request.couponId(),
				request.items()
						.stream()
						.map(item -> PlaceOrderCommand.CreateOrderItem.of(item.productId(), item.quantity()))
						.toList()
		);

		OrderInfo orderInfo = orderFacade.placeOrder(command);

		return ApiResponse.success(PlaceOrderResponse.from(orderInfo));
	}
}
