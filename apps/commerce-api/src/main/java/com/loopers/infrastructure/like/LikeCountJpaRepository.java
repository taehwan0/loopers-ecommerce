package com.loopers.infrastructure.like;

import com.loopers.domain.like.LikeCountEntity;
import com.loopers.domain.like.LikeTargetType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeCountJpaRepository extends JpaRepository<LikeCountEntity, Long> {
	@Query("SELECT lk FROM LikeCountEntity lk WHERE lk.target.targetId = :targetId AND lk.target.targetType = :likeTargetType")
	Optional<LikeCountEntity> findByTargetId(Long targetId, LikeTargetType likeTargetType);
}
