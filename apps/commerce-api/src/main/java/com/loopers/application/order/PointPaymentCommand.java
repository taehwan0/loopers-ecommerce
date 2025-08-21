package com.loopers.application.order;

public record PointPaymentCommand(
		Long orderId
) {
	public static PointPaymentCommand of(Long orderId) {
		return new PointPaymentCommand(orderId);
	}
}
