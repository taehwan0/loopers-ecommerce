package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Point {
	@Column(name = "pointValue", nullable = false)
	private int pointValue;

	protected Point() {}

	public Point(int pointValue) {
		this.pointValue = pointValue;
	}

	protected void addPoint(int pointValue) {
		if (pointValue <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "1이상의 포인트만 충전 가능합니다.");
		}
		this.pointValue += pointValue;
	}
}
