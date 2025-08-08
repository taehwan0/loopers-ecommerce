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
}
