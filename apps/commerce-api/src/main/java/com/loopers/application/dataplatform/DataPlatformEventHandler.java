package com.loopers.application.dataplatform;

import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentEvent;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.push.DataPlatformService;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataPlatformEventHandler {

	private final DataPlatformService dataPlatformService;
	private final UserService userService;
	private final OrderService orderService;
	private final ProductService productService;
	private final PaymentService paymentService;

	public void sendOrderSuccessPush(PaymentEvent.PaymentSuccess event) {
		var order = orderService.getOrder(event.orderId());
		var payment =  paymentService.getById(event.paymentId());

		var user = userService.getUser(order.getUserId())
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "사용자를 찾을 수 없습니다."));

		dataPlatformService.sendOrderSuccessPush(
				new DataPlatformService.UserInfo(
						user.getLoginId(),
						user.getName()
				),
				new DataPlatformService.OrderInfo(
						order.getOrderStatus().name(),
						order.getOrderItems()
								.stream()
								.map(item -> new DataPlatformService.OrderItemInfo(
										productService.getProduct(item.getProductId()).getName(),
										item.getQuantity()
								))
								.toList()
				),
				new DataPlatformService.PaymentInfo(
						payment.getPaymentMethod().name(),
						payment.getAmount()
				)
		);
	}
}
