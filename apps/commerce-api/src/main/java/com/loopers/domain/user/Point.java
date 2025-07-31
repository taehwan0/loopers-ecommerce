package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Embeddable
public class Point {
	@Column(name = "point_value", nullable = false)
	private int pointValue;

	private Point(int pointValue) {
		if (pointValue < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "포인트는 음수가 될 수 없습니다.");
		}

		this.pointValue = pointValue;
	}

	public static Point of(int pointValue) {
		return new Point(pointValue);
	}

	protected void addPoint(int pointValue) {
		if (pointValue <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "1이상의 포인트만 충전 가능합니다.");
		}
		this.pointValue += pointValue;
	}
}
