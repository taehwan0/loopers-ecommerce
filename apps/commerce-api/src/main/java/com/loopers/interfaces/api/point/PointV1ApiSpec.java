package com.loopers.interfaces.api.point;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.point.PointV1Dto.ChargePointRequest;
import com.loopers.interfaces.api.point.PointV1Dto.PointResponse;
import io.swagger.v3.oas.annotations.Operation;

public interface PointV1ApiSpec {
	@Operation(
			summary = "회원 포인트 조회",
			description = "사용자ID로 사용자 포인트를 조회합니다."
	)
	ApiResponse<PointResponse> getPoint(String loginId);

	@Operation(
			summary = "회원 포인트 충전",
			description = "요청 대상의 포인트를 충전합니다."
	)
	ApiResponse<PointResponse> chargePoint(String loginId, ChargePointRequest request);
}
