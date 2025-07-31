package com.loopers.infrastructure.like;

import com.loopers.domain.like.LikeEntity;
import com.loopers.domain.like.LikeRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class LikeRepositoryImpl implements LikeRepository {

	private final LikeJpaRepository likeJpaRepository;

	@Override
	public Optional<LikeEntity> findByUserIdAndProductId(Long userId, Long productId) {
		return likeJpaRepository.findByUserIdAndProductId(userId, productId);
	}

	@Override
	public LikeEntity save(LikeEntity like) {
		return likeJpaRepository.save(like);
	}

	@Override
	public void delete(LikeEntity like) {
		likeJpaRepository.delete(like);
	}
}
