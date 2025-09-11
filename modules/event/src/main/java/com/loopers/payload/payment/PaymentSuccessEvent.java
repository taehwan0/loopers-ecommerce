package com.loopers.payload.payment;

import com.loopers.payload.EventPayload;

public record PaymentSuccessEvent(Long orderId, Long paymentId, String transactionKey) implements EventPayload {

	public static PaymentSuccessEvent of(Long orderId, Long paymentId, String transactionKey) {
		return new PaymentSuccessEvent(orderId, paymentId, transactionKey);
	}
}
