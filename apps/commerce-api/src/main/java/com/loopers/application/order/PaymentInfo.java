package com.loopers.application.order;

import com.loopers.domain.payment.PaymentEntity;

public record PaymentInfo(
		Long orderId,
		String paymentMethod,
		String paymentStatus,
		String transactionKey // nullable
) {

	public static PaymentInfo from(PaymentEntity payment) {
		return new PaymentInfo(
				payment.getOrderId(),
				payment.getPaymentMethod().name(),
				payment.getPaymentStatus().name(),
				payment.getTransactionKey()
		);
	}
}
