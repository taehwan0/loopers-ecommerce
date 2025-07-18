package com.loopers.application.user;

import com.loopers.domain.user.Point;

public record PointInfo (
		int pointValue
) {
	public static PointInfo from(Point point) {
		return new PointInfo(point.getPointValue());
	}
}
