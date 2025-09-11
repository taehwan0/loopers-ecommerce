package com.loopers.interfaces.listener.like;

import com.loopers.application.like.LikeEventHandler;
import com.loopers.payload.like.ProductLikedEvent;
import com.loopers.payload.like.ProductUnlikedEvent;
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
	public void handleLikeEvent(ProductLikedEvent event) {
		likeEventHandler.increaseProductLikeCount(event);
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleUnlikeEvent(ProductUnlikedEvent event) {
		likeEventHandler.decreaseProductLikeCount(event);
	}
}
