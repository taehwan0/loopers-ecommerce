package com.loopers.payload.payment;

import com.loopers.payload.EventPayload;

public record PaymentFailEvent(Long orderId, Long paymentId, String transactionKey) implements EventPayload {

	public static PaymentFailEvent of(Long orderId, Long paymentId, String transactionKey) {
		return new PaymentFailEvent(orderId, paymentId, transactionKey);
	}
}
