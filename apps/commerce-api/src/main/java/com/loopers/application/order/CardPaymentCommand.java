package com.loopers.application.order;

public record CardPaymentCommand(
		Long orderId,
		String cardType,
		String cardNumber
) {
	public static CardPaymentCommand of(Long orderId, String cardType, String cardNumber) {
		return new CardPaymentCommand(orderId, cardType, cardNumber);
	}
}
