package com.loopers.domain.like;

import java.util.Optional;

public interface LikeCountRepository {
	Optional<LikeCountEntity> getTargetLikeCount(Long targetId, LikeTargetType targetType);

	LikeCountEntity save(LikeCountEntity likeCount);
}
