package com.loopers.interfaces.listener.like;

import com.loopers.application.like.LikeFacade;
import com.loopers.domain.like.LikeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class LikeListener {

	private final LikeFacade likeFacade;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleLikeEvent(LikeEvent.ProductLike productLikeEvent) {
		likeFacade.increaseProductLikeCount(productLikeEvent.productId());
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleUnlikeEvent(LikeEvent.ProductUnlike productUnlikeEvent) {
		likeFacade.decreaseProductLikeCount(productUnlikeEvent.productId());
	}
}
