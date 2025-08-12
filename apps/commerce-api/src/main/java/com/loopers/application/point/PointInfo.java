package com.loopers.application.point;

import com.loopers.domain.point.PointAccountEntity;

public record PointInfo (
		long pointValue
) {
	public static PointInfo from(PointAccountEntity pointAccount) {
		return new PointInfo(pointAccount.getPointBalance().getValue().longValue());
	}
}
