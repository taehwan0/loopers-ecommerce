package com.loopers.domain.like;

import com.loopers.payload.like.ProductLikedEvent;
import com.loopers.payload.like.ProductUnlikedEvent;

public interface LikeEventPublisher {

	void publish(ProductLikedEvent event);

	void publish(ProductUnlikedEvent event);
}
