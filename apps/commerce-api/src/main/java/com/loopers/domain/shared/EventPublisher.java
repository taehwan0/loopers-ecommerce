package com.loopers.domain.shared;

public interface EventPublisher {

	void publish(Event<?> event);
}
