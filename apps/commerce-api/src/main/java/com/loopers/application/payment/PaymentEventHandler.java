package com.loopers.application.payment;

import com.loopers.domain.order.OrderEntity;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentEntity;
import com.loopers.domain.payment.PaymentEvent;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PaymentEventHandler {

	private final OrderService orderService;
	private final PaymentService paymentService;
	private final ProductService productService;

	@Transactional
	public void paymentSuccess(PaymentEvent.PaymentSuccess event) {
		OrderEntity order = orderService.getOrder(event.orderId());
		order.paymentConfirm();

		PaymentEntity payment = paymentService.getById(event.paymentId());
		payment.success();
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
