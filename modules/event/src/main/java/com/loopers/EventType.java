package com.loopers;

import com.loopers.payload.like.ProductLikedEvent;
import com.loopers.payload.like.ProductUnlikedEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public enum EventType {
	PRODUCT_LIKED(ProductLikedEvent.class, Topic.PRODUCT_LIKE_CHANGED),
	PRODUCT_UNLIKED(ProductUnlikedEvent.class, Topic.PRODUCT_LIKE_CHANGED),
	;

	private final Class<?> payloadClass;
	private final String topic;

	EventType(Class<?> payloadClass, String topic) {
		this.payloadClass = payloadClass;
		this.topic = topic;
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Topic {
		public static final String PRODUCT_LIKE_CHANGED = "product.v1.like-changed";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ConsumerGroup {
		public static final String PRODUCT_METRICS_CONSUMER = "product-metrics-consumer";
	}
}
