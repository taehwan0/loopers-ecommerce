package com.loopers.domain.like;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeTarget {

	@Column(name = "target_id", nullable = false)
	private Long targetId;

	@Enumerated(EnumType.STRING)
	@Column(name = "target_type", nullable = false)
	private LikeTargetType targetType;

	private LikeTarget(Long targetId, LikeTargetType targetType) {
		if (targetId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "targetId는 비어있을 수 없습니다.");
		}

		if (targetType == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "targetType은 비어있을 수 없습니다.");
		}

		this.targetId = targetId;
		this.targetType = targetType;
	}

	public static LikeTarget of(Long targetId, LikeTargetType targetType) {
		return new LikeTarget(targetId, targetType);
	}
}
