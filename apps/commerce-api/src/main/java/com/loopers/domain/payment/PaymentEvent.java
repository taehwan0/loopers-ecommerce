package com.loopers.domain.payment;

public class PaymentEvent {

	public record PaymentSuccess(
			Long orderId,
			Long paymentId
	) {
		public static PaymentSuccess of(Long orderId, Long paymentId) {
			return new PaymentSuccess(orderId, paymentId);
		}
	}

	public record PaymentFail(
			Long orderId,
			Long paymentId
	) {
		public static PaymentFail of(Long orderId, Long paymentId) {
			return new PaymentFail(orderId, paymentId);
		}
	}
}
