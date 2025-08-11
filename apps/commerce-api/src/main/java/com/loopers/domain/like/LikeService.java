package com.loopers.domain.like;

import java.util.Optional;
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

	@Transactional
	public void likeProduct(Long userId, Long productId) {
		Optional<LikeEntity> likeOptional = likeRepository.findByUserIdAndProductId(userId, productId);

		if (likeOptional.isPresent()) {
			return;
		}

		likeCountRepository.getTargetLikeCountWithPessimisticWriteLock(productId, LikeTargetType.PRODUCT)
				.ifPresentOrElse(likeCount -> {
							likeRepository.save(LikeEntity.of(userId, LikeTarget.of(productId, LikeTargetType.PRODUCT)));
							likeCount.increaseLikeCount();
						},
						() -> {
							LikeTarget likeTarget = LikeTarget.of(productId, LikeTargetType.PRODUCT);
							likeRepository.save(LikeEntity.of(userId, likeTarget));

							LikeCountEntity likeCount = likeCountRepository.save(LikeCountEntity.of(likeTarget));
							likeCount.increaseLikeCount();
						}
				);
	}

	@Transactional
	public void unlikeProduct(Long userId, Long productId) {
		Optional<LikeEntity> likeOptional = likeRepository.findByUserIdAndProductId(userId, productId);

		if (likeOptional.isEmpty()) {
			return;
		}

		likeCountRepository.getTargetLikeCountWithPessimisticWriteLock(productId, LikeTargetType.PRODUCT)
				.ifPresent(likeCount -> {
							likeRepository.save(LikeEntity.of(userId, LikeTarget.of(productId, LikeTargetType.PRODUCT)));
							likeCount.decreaseLikeCount();
						}
				);
	}

	public LikeCountEntity getProductLikeCount(Long productId) {
		return likeCountRepository.getTargetLikeCount(productId, LikeTargetType.PRODUCT)
				.orElseGet(() -> initLikeCount(productId, LikeTargetType.PRODUCT));
	}
}
