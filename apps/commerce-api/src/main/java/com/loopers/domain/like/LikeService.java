package com.loopers.domain.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class LikeService {

	private final LikeRepository likeRepository;
	private final LikeCountRepository likeCountRepository;

	public LikeCountEntity initLikeCount(Long targetId, LikeTargetType targetType) {
		LikeTarget likeTarget = LikeTarget.of(targetId, targetType);
		return likeCountRepository.save(LikeCountEntity.of(likeTarget));
	}

	public void likeProduct(Long userId, Long productId) {
		if (likeRepository.findByUserIdAndProductId(userId, productId).isPresent()) {
			return;
		}

		likeRepository.save(LikeEntity.of(userId, LikeTarget.of(productId, LikeTargetType.PRODUCT)));
	}

	public void unlikeProduct(Long userId, Long productId) {
		likeRepository.findByUserIdAndProductId(userId, productId)
				.ifPresent(likeRepository::delete);
	}

	@Transactional
	public void increaseProductLikeCount(Long productId) {
		likeCountRepository.getTargetLikeCountWithPessimisticWriteLock(productId, LikeTargetType.PRODUCT)
				.ifPresentOrElse(
						LikeCountEntity::increaseLikeCount,
						() -> {
							LikeCountEntity likeCount = initLikeCount(productId, LikeTargetType.PRODUCT);
							likeCount.increaseLikeCount();
						});
	}

	@Transactional
	public void decreaseProductLikeCount(Long productId) {
		likeCountRepository.getTargetLikeCountWithPessimisticWriteLock(productId, LikeTargetType.PRODUCT)
				.ifPresentOrElse(
						LikeCountEntity::decreaseLikeCount,
						() -> initLikeCount(productId, LikeTargetType.PRODUCT)
				);
	}
}
