package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointFacade;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.point.PointV1Dto.ChargePointRequest;
import com.loopers.interfaces.api.point.PointV1Dto.PointResponse;
import com.loopers.support.RequireUserLoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/points")
@RestController
public class PointV1Controller implements PointV1ApiSpec {

	public static final String X_USER_ID_HEADER = "X-USER-ID";
	private final PointFacade pointFacade;

	@Override
	@RequireUserLoginId
	@GetMapping("")
	public ApiResponse<PointResponse> getPoint(
			@RequestHeader(X_USER_ID_HEADER) String loginId
	) {
		return ApiResponse.success(PointResponse.from(pointFacade.getUserPoint(loginId)));
	}

	@Override
	@RequireUserLoginId
	@PostMapping("/charge")
	public ApiResponse<PointResponse> chargePoint(
			@RequestHeader(X_USER_ID_HEADER) String loginId,
			@RequestBody ChargePointRequest request
	) {
		return ApiResponse.success(PointResponse.from(pointFacade.chargePoint(loginId, request.amount())));
	}
}
