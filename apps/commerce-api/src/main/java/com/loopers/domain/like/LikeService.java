package com.loopers.domain.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LikeService {

	private final LikeRepository likeRepository;
	private final LikeCountRepository likeCountRepository;

	public void likeProduct(Long userId, Long productId) {
		if (likeRepository.findByUserIdAndProductId(userId, productId).isEmpty()) {
			likeRepository.save(LikeEntity.of(userId, LikeTarget.of(productId, LikeTargetType.PRODUCT)));
			getProductLikeCount(productId).increaseLikeCount();
		}
	}

	public void unlikeProduct(Long userId, Long productId) {
		likeRepository.findByUserIdAndProductId(userId, productId)
				.ifPresent(like -> {
					likeRepository.delete(like);
					getProductLikeCount(productId).decreaseLikeCount();
				});
	}

	public LikeCountEntity getProductLikeCount(Long productId) {
		return likeCountRepository.getTargetLikeCount(productId, LikeTargetType.PRODUCT)
				.orElseGet(() -> likeCountRepository.save(LikeCountEntity.of(LikeTarget.of(productId, LikeTargetType.PRODUCT))));
	}
}
