package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.user.UserV1Dto.ChargePointRequest;
import com.loopers.interfaces.api.user.UserV1Dto.RegisterUserRequest;
import com.loopers.interfaces.api.user.UserV1Dto.UserPointResponse;
import com.loopers.interfaces.api.user.UserV1Dto.UserResponse;
import com.loopers.support.RequireUserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserV1Controller implements UserV1ApiSpec {

	public static final String X_USER_ID_HEADER = "X-USER-ID";
	private final UserFacade userFacade;

	@PostMapping("")
	@Override
	public ApiResponse<UserResponse> registerUser(
			@Valid @RequestBody RegisterUserRequest request
	) {

		UserInfo userInfo = userFacade.register(
				request.userId(),
				request.name(),
				request.gender().toString(),
				request.birth(),
				request.email()
		);
		return ApiResponse.success(UserV1Dto.UserResponse.from(userInfo));
	}

	@Override
	@GetMapping("/{id}")
	public ApiResponse<UserResponse> getUser(@PathVariable Long id) {
		return ApiResponse.success(UserV1Dto.UserResponse.from(userFacade.getUser(id)));
	}

	@Override
	@RequireUserId
	@GetMapping("/points")
	public ApiResponse<UserPointResponse> getUserPoint(
			@RequestHeader(X_USER_ID_HEADER) String userId
	) {
		return ApiResponse.success(UserPointResponse.from(userFacade.getUserPoint(userId)));
	}

	@Override
	@RequireUserId
	@PostMapping("/points")
	public ApiResponse<UserPointResponse> getUserPoint(
			@RequestHeader(X_USER_ID_HEADER) String userId,
			@RequestBody ChargePointRequest request
	) {
		return ApiResponse.success(UserPointResponse.from(userFacade.chargePoint(userId, request.amount())));
	}
}
