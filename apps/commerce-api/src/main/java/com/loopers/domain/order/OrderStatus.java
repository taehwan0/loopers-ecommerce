package com.loopers.domain.order;

public enum OrderStatus {
	PENDING,
	PAYMENT_FAILED,
	PAYMENT_CONFIRMED,
	SHIPPED,
	DELIVERED,
	CANCELLED,
	RETURNED,
}
