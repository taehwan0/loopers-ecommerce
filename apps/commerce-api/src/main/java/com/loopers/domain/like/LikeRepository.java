package com.loopers.domain.like;

import java.util.Optional;

public interface LikeRepository {
	Optional<LikeEntity> findByUserIdAndProductId(Long userId, Long productId);

	LikeEntity save(LikeEntity like);

	void delete(LikeEntity like);
}
