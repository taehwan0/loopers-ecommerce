package com.loopers.domain.shared;

import java.util.UUID;

public record Event<T>(
		UUID eventId,
		T payload
) {
	public static <T> Event<T> of(T payload) {
		return new Event<>(UUID.randomUUID(), payload);
	}
}
