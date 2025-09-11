package com.loopers;

import com.loopers.payload.EventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	public void publish(EventPayload event) {
		applicationEventPublisher.publishEvent(event);

	}
}
