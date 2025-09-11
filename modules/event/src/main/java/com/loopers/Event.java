package com.loopers;

import com.loopers.payload.EventPayload;

public record Event<T extends EventPayload>(String eventId, EventType eventType, T payload) {

	public static <T extends EventPayload> Event<T> of(String eventId, EventType eventType, T payload) {
		return new Event<>(eventId, eventType, payload);
	}
}
