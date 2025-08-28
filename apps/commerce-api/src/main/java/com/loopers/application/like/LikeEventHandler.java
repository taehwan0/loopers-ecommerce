package com.loopers.application.like;

import com.loopers.domain.like.LikeEvent;
import com.loopers.domain.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LikeEventHandler {

	private final LikeService likeService;

	public void increaseProductLikeCount(LikeEvent.ProductLike event) {
		likeService.increaseProductLikeCount(event.productId());
	}

	public void decreaseProductLikeCount(LikeEvent.ProductUnlike event) {
		likeService.decreaseProductLikeCount(event.productId());
	}
}
