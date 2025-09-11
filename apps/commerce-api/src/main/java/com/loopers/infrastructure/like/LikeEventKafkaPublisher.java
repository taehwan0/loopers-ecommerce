package com.loopers.infrastructure.like;

import com.loopers.Event;
import com.loopers.EventType;
import com.loopers.domain.like.LikeEventPublisher;
import com.loopers.payload.like.ProductLikedEvent;
import com.loopers.payload.like.ProductUnlikedEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class LikeEventKafkaPublisher implements LikeEventPublisher {

	private final KafkaTemplate<Object, Object> kafkaTemplate;

	@Override
	public void publish(ProductLikedEvent event) {
		var kafkaEvent = Event.of(
				UUID.randomUUID().toString(),
				EventType.PRODUCT_LIKED,
				event
		);

		kafkaTemplate.send(kafkaEvent.topic(), kafkaEvent);
	}

	@Override
	public void publish(ProductUnlikedEvent event) {
		var kafkaEvent = Event.of(
				UUID.randomUUID().toString(),
				EventType.PRODUCT_UNLIKED,
				event
		);

		kafkaTemplate.send(kafkaEvent.topic(), kafkaEvent);
	}
}
