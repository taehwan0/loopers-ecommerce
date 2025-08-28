package com.loopers.domain.like;

public class LikeEvent {
	public record ProductLike(
			Long productId,
			Long userId
	) {
		public static ProductLike of(Long productId, Long userId) {
			return new ProductLike(productId, userId);
		}

	}

	public record ProductUnlike(
			Long productId,
			Long userId
	) {
		public static ProductUnlike of(Long productId, Long userId) {
			return new ProductUnlike(productId, userId);
		}
	}
}
