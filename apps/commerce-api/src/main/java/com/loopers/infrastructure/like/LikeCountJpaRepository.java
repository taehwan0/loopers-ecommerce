package com.loopers.infrastructure.like;

import com.loopers.domain.like.LikeCountEntity;
import com.loopers.domain.like.LikeTargetType;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeCountJpaRepository extends JpaRepository<LikeCountEntity, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT lk FROM LikeCountEntity lk WHERE lk.target.targetId = :targetId AND lk.target.targetType = :likeTargetType")
	Optional<LikeCountEntity> findByTargetIdWithPessimisticWriteLock(@Param("targetId") Long targetId, @Param("likeTargetType") LikeTargetType likeTargetType);

	@Query("SELECT lk FROM LikeCountEntity lk WHERE lk.target.targetId = :targetId AND lk.target.targetType = :likeTargetType")
	Optional<LikeCountEntity> findByTargetId(@Param("targetId") Long targetId, @Param("likeTargetType") LikeTargetType likeTargetType);
}
