package com.loopers.infrastructure.like;

import com.loopers.domain.like.LikeCountEntity;
import com.loopers.domain.like.LikeCountRepository;
import com.loopers.domain.like.LikeTargetType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LikeCountRepositoryImp implements LikeCountRepository {

	private final LikeCountJpaRepository likeCountJpaRepository;

	@Override
	public Optional<LikeCountEntity> getTargetLikeCountWithPessimisticWriteLock(Long targetId, LikeTargetType targetType) {
		return likeCountJpaRepository.findByTargetIdWithPessimisticWriteLock(targetId, targetType);
	}

	@Override
	public Optional<LikeCountEntity> getTargetLikeCount(Long targetId, LikeTargetType targetType) {
		return likeCountJpaRepository.findByTargetId(targetId, targetType);
	}

	@Override
	public LikeCountEntity save(LikeCountEntity likeCount) {
		return likeCountJpaRepository.save(likeCount);
	}
}
