package com.loopers.infrastructure.shared;

import com.loopers.domain.shared.Event;
import com.loopers.domain.shared.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EventPublisherImpl implements EventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void publish(Event<?> event) {
		log.info("Publishing event: {}", event);
		applicationEventPublisher.publishEvent(event.payload());
	}
}
