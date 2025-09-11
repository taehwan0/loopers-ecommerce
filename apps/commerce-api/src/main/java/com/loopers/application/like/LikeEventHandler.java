package com.loopers.application.like;

import com.loopers.domain.like.LikeService;
import com.loopers.payload.like.ProductLikedEvent;
import com.loopers.payload.like.ProductUnlikedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LikeEventHandler {

	private final LikeService likeService;

	public void increaseProductLikeCount(ProductLikedEvent event) {
		likeService.increaseProductLikeCount(event.productId());
	}

	public void decreaseProductLikeCount(ProductUnlikedEvent event) {
		likeService.decreaseProductLikeCount(event.productId());
	}
}
