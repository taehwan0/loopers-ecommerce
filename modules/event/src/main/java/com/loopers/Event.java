package com.loopers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.loopers.payload.EventPayload;

public record Event<T extends EventPayload>(String eventId, EventType eventType, T payload) {

	private static final ObjectMapper MAPPER = new ObjectMapper()
			.registerModule(new JavaTimeModule())
			.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

	public static <T extends EventPayload> Event<T> of(String eventId, EventType eventType, T payload) {
		return new Event<>(eventId, eventType, payload);
	}

	public String topic() {
		return this.eventType.getTopic();
	}

	public static Event<EventPayload> fromJson(String json) {
		try {
			// 1) envelope(헤더)만 먼저 읽기
			Envelope env = MAPPER.readValue(json, Envelope.class);

			// 2) eventType으로 payload의 구체 클래스를 결정
			EventType type = EventType.valueOf(env.eventType);
			@SuppressWarnings("unchecked")
			Class<? extends EventPayload> payloadClass =
					(Class<? extends EventPayload>) type.getPayloadClass();

			if (payloadClass == null) {
				throw new IllegalArgumentException("Unsupported eventType: " + type);
			}

			// 3) payload(JsonNode) → 구체 클래스 역직렬
			EventPayload payload = MAPPER.treeToValue(env.payload, payloadClass);

			// 4) 최종 Event 조립
			return Event.of(env.eventId, type, payload);

		} catch (Exception e) {
			throw new IllegalArgumentException("Failed to deserialize Event from json", e);
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	private static final class Envelope {
		public String eventId;
		public String eventType;
		public JsonNode payload;
	}
}
