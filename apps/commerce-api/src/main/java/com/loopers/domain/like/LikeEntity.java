package com.loopers.domain.like;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_like")
public class LikeEntity extends BaseEntity {

	@Column(name = "user_id", nullable = false, updatable = false)
	private Long userId;

	@Embedded
	private LikeTarget likeTarget;

	private LikeEntity(Long userId, LikeTarget likeTarget) {
		if (userId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "userId는 비어있을 수 없습니다.");
		}

		if (likeTarget == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "likeTarget은 비어있을 수 없습니다.");
		}

		this.userId = userId;
		this.likeTarget = likeTarget;
	}

	public static LikeEntity createProductLike(Long userId, LikeTarget likeTarget) {
		return new LikeEntity(userId, likeTarget);
	}
}
