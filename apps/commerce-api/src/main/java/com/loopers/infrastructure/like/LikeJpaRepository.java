package com.loopers.infrastructure.like;

import com.loopers.domain.like.LikeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeJpaRepository extends JpaRepository<LikeEntity, Long> {
	@Query("SELECT l FROM LikeEntity l WHERE l.userId = :userId AND l.likeTarget.targetId = :productId AND l.likeTarget.targetType = 'PRODUCT'")
	Optional<LikeEntity> findByUserIdAndProductId(Long userId, Long productId);
}
