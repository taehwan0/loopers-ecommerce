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
@Table(name = "like_count")
public class LikeCountEntity extends BaseEntity {
	@Embedded
	LikeTarget target;

	@Column(name = "like_count", nullable = false)
	private int likeCount;

	private LikeCountEntity(LikeTarget target, int likeCount) {
		if (target == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "target는 비어있을 수 없습니다.");
		}

		if (likeCount < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "likeCount는 음수가 될 수 없습니다.");
		}

		this.target = target;
		this.likeCount = likeCount;
	}

	public static LikeCountEntity of(LikeTarget target) {
		return new LikeCountEntity(target, 0);
	}

	public void increaseLikeCount() {
		this.likeCount++;
	}

	public void decreaseLikeCount() {
		this.likeCount--;
	}
}
