package com.loopers.interfaces.api.payment;

import com.loopers.application.order.PaymentInfo;

public class PaymentV1Dto {

	public record PointPaymentRequest(
			Long orderId
	) {

	}

	public record CardPaymentRequest(
			Long orderId,
			String cardType,
			String cardNumber
	) {

	}

	public record PaymentResponse(
			Long orderId,
			String paymentMethod,
			String paymentStatus,
			String transactionKey // nullable
	) {

		public static PaymentResponse from(PaymentInfo paymentInfo) {
			return new PaymentResponse(
					paymentInfo.orderId(),
					paymentInfo.paymentMethod(),
					paymentInfo.paymentStatus(),
					paymentInfo.transactionKey()
			);
		}
	}

	public record CallbackRequest(
			String transactionKey,
			String orderId,
			String cardType,
			String cardNo,
			Long amount,
			String status,
			String reason // Nullable
	) {
	}
}
