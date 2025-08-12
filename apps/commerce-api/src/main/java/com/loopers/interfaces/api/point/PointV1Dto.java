package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointInfo;
import jakarta.validation.constraints.Min;

public class PointV1Dto {

	public record ChargePointRequest(
			@Min(value = 1, message = "포인트는 1 이상이어야 합니다.")
			int amount
	) {

	}

	public record PointResponse(long pointValue) {
		public static PointResponse from(PointInfo pointInfo) {
			return new PointResponse(pointInfo.pointValue());
		}
	}
}
