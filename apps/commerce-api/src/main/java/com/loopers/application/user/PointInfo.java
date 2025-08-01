package com.loopers.application.user;

import com.loopers.domain.user.Point;

public record PointInfo (
		long pointValue
) {
	public static PointInfo from(Point point) {
		return new PointInfo(point.getPointValue());
	}
}
