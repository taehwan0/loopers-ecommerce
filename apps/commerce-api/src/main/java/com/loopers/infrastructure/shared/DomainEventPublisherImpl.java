package com.loopers.infrastructure.shared;

import com.loopers.domain.shared.DomainEvent;
import com.loopers.domain.shared.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DomainEventPublisherImpl implements DomainEventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void publish(DomainEvent<?> event) {
		log.info("Publishing event: {}", event);
		applicationEventPublisher.publishEvent(event.payload());
	}
}
