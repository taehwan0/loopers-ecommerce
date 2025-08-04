package com.loopers.interfaces.api.user;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User V1 API", description = "Loopers 사용자 API")
public interface UserV1ApiSpec {

	@Operation(
			summary = "회원가입",
			description = "사용자 정보를 등록합니다."
	)
	ApiResponse<UserV1Dto.UserResponse> registerUser(UserV1Dto.RegisterUserRequest request);

	@Operation(
			summary = "회원 조회",
			description = "ID로 사용자를 조회합니다."
	)
	ApiResponse<UserV1Dto.UserResponse> getUser(Long id);

	@Operation(
			summary = "회원 포인트 조회",
			description = "사용자ID로 사용자 포인트를 조회합니다."
	)
	ApiResponse<UserV1Dto.UserPointResponse> getUserPoint(String loginId);

	@Operation(
			summary = "회원 포인트 충전",
			description = "요청 대상의 포인트를 충전합니다."
	)
	ApiResponse<UserV1Dto.UserPointResponse> getUserPoint(String loginId, UserV1Dto.ChargePointRequest request);
}
