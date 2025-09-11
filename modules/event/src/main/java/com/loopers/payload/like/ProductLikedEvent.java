package com.loopers.payload.like;

import com.loopers.payload.EventPayload;

public record ProductLikedEvent(Long productId) implements EventPayload {

	public static ProductLikedEvent of(Long productId) {
		return new ProductLikedEvent(productId);
	}
}
