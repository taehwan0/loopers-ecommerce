package com.loopers.payload.like;

import com.loopers.payload.EventPayload;

public record ProductUnlikedEvent(Long productId) implements EventPayload {

	public static ProductUnlikedEvent of(Long productId) {
		return new ProductUnlikedEvent(productId);
	}
}
