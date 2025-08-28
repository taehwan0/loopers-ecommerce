package com.loopers.domain.shared;

public interface DomainEventPublisher {

	void publish(DomainEvent<?> event);
}
