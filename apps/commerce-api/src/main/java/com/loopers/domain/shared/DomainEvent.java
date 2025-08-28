package com.loopers.domain.shared;

import java.util.UUID;

public record DomainEvent<T>(
		UUID eventId,
		T payload
) {
	public static <T> DomainEvent<T> of(T payload) {
		return new DomainEvent<>(UUID.randomUUID(), payload);
	}
}
