package com.loopers.infrastructure.payment.pgsimul;

import java.util.List;

public class PgSimulDTO {

	public record PaymentRequest(
			String orderId,
			String cardType,
			String cardNo,
			long amount,
			String callbackUrl
	) {

	}

	public record PaymentResponse(
			String transactionKey,
			String status
	) {

	}

	public record PaymentInfoResponse(
			String transactionKey,
			String orderId,
			String cardType,
			String cardNo,
			long amount,
			Status status,
			String reason
	) {

	}

	public record OrderPaymentInfoResponse(
			String orderId,
			List<TransactionInfo> transactions
	) {

		public record TransactionInfo(
				String transactionKey,
				Status status,
				String reason
		) {

		}
	}

	public enum Status {
		SUCCESS, FAIL
	}
}
