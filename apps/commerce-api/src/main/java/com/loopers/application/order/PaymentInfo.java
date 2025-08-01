package com.loopers.application.order;

import com.loopers.domain.order.OrderEntity;
import com.loopers.domain.payment.PaymentEntity;

public record PaymentInfo(
		PaymentSummary payment,
		OrderSummary order
) {

	public record PaymentSummary(
			String paymentMethod,
			long totalAmount
	) {

	}

	public record OrderSummary(
			Long orderId,
			String orderStatus
	) {

	}

	public static PaymentInfo from(PaymentEntity payment, OrderEntity order) {
		return new PaymentInfo(
				new PaymentSummary(payment.getPaymentMethod().name(), payment.getAmount()),
				new OrderSummary(order.getId(), order.getOrderStatus().name())
		);
	}
}
