package com.loopers.application.payment;

public record PaymentCallbackCommand(
		String transactionKey,
		String orderId,
		CardType cardType,
		String cardNo,
		Long amount,
		TransactionStatus status,
		String reason // Nullable
) {

	public static PaymentCallbackCommand of(
			String transactionKey,
			String orderId,
			String cardType,
			String cardNo,
			Long amount,
			String status,
			String reason
	) {
		return new PaymentCallbackCommand(
				transactionKey,
				orderId,
				CardType.of(cardType),
				cardNo,
				amount,
				TransactionStatus.of(status),
				reason
		);
	}

	public enum CardType {
		HYUNDAI,
		KB,
		SAMSUNG;

		public static CardType of(String value) {
			for (CardType cardType : values()) {
				if (cardType.name().equalsIgnoreCase(value)) {
					return cardType;
				}
			}
			throw new IllegalArgumentException("Invalid card type: " + value);
		}
	}

	public enum TransactionStatus {
		PENDING,
		SUCCESS,
		FAIL;

		public static TransactionStatus of(String value) {
			for (TransactionStatus status : values()) {
				if (status.name().equalsIgnoreCase(value)) {
					return status;
				}
			}
			throw new IllegalArgumentException("Invalid transaction status: " + value);
		}
	}
}
