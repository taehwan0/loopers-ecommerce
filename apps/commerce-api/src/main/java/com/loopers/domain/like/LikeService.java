package com.loopers.domain.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LikeService {

	private final LikeRepository likeRepository;

	public void likeProduct(Long userId, Long productId) {
		if (likeRepository.findByUserIdAndProductId(userId, productId).isEmpty()) {
			likeRepository.save(LikeEntity.of(userId, LikeTarget.of(productId, LikeTargetType.PRODUCT)));
		}
	}

	public void unlikeProduct(Long userId, Long productId) {
		likeRepository.findByUserIdAndProductId(userId, productId)
				.ifPresent(likeRepository::delete);
	}
}
