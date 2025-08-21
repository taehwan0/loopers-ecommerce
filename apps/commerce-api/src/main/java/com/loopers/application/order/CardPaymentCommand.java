package com.loopers.application.order;

public record CardPaymentCommand(
		Long orderId,
		String cardType,
		String cardNumber
) {

}
