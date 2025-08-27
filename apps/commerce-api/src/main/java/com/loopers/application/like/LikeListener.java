package com.loopers.application.like;

import com.loopers.domain.like.LikeEvent;
import com.loopers.domain.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class LikeListener {

	private final LikeService likeService;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleLikeEvent(LikeEvent.ProductLike productLikeEvent) {
		likeService.increaseProductLikeCount(productLikeEvent.productId());
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleUnlikeEvent(LikeEvent.ProductUnlike productUnlikeEvent) {
		likeService.decreaseProductLikeCount(productUnlikeEvent.productId());
	}
}
