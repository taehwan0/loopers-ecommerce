package com.loopers.application.payment;

import com.loopers.domain.order.OrderEntity;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentEntity;
import com.loopers.domain.payment.PaymentEvent;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.push.PushService;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentEventHandler {

	private final OrderService orderService;
	private final PaymentService paymentService;
	private final UserService userService;
	private final PushService pushService;
	private final ProductService productService;

	@Transactional
	public void paymentSuccess(PaymentEvent.PaymentSuccess event) {
		OrderEntity order = orderService.getOrder(event.orderId());
		order.paymentConfirm();

		PaymentEntity payment = paymentService.getById(event.paymentId());
		payment.success();

		var user = userService.getUser(order.getUserId())
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "사용자를 찾을 수 없습니다."));

		pushService.sendOrderSuccessPush(
				new PushService.UserInfo(
						user.getLoginId(),
						user.getName()
				),
				new PushService.OrderInfo(
						order.getOrderStatus().name(),
						order.getOrderItems()
								.stream()
								.map(item -> new PushService.OrderItemInfo(
										productService.getProduct(item.getProductId()).getName(),
										item.getQuantity()
								))
								.toList()
				),
				new PushService.PaymentInfo(
						payment.getPaymentMethod().name(),
						payment.getAmount()
				)
		);
	}

	@Transactional
	public void paymentFail(PaymentEvent.PaymentFail event) {
		OrderEntity order = orderService.getOrder(event.orderId());
		order.paymentFailed();

		PaymentEntity payment = paymentService.getById(event.paymentId());
		payment.fail();

		order.getOrderItems().forEach(item -> productService.increaseStock(item.getProductId(), item.getQuantity()));
	}
}
