package com.loopers.application.like;

import com.loopers.domain.like.LikeEventPublisher;
import com.loopers.domain.like.LikeService;
import com.loopers.payload.like.ProductLikedEvent;
import com.loopers.payload.like.ProductUnlikedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LikeEventHandler {

	private final LikeService likeService;
	private final LikeEventPublisher likeEventPublisher;

	public void increaseProductLikeCount(ProductLikedEvent event) {
		likeService.increaseProductLikeCount(event.productId());
		likeEventPublisher.publish(event);
	}

	public void decreaseProductLikeCount(ProductUnlikedEvent event) {
		likeService.decreaseProductLikeCount(event.productId());
		likeEventPublisher.publish(event);
	}
}
