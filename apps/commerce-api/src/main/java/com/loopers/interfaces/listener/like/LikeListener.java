package com.loopers.interfaces.listener.like;

import com.loopers.application.like.LikeEventHandler;
import com.loopers.domain.like.LikeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class LikeListener {

	private final LikeEventHandler likeEventHandler;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleLikeEvent(LikeEvent.ProductLike productLikeEvent) {
		likeEventHandler.increaseProductLikeCount(productLikeEvent);
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleUnlikeEvent(LikeEvent.ProductUnlike productUnlikeEvent) {
		likeEventHandler.decreaseProductLikeCount(productUnlikeEvent);
	}
}
