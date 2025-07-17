package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserInfo;
import jakarta.validation.constraints.NotNull;

public class UserV1Dto {

	public record UserResponse(
			Long id,
			String userId,
			String name,
			String gender,
			String birth,
			String email
	) {

		static UserResponse from(UserInfo userInfo) {
			return new UserResponse(
					userInfo.id(),
					userInfo.userId(),
					userInfo.name(),
					userInfo.gender().toString(),
					userInfo.birth().toString(),
					userInfo.email()
			);
		}
	}


	public record RegisterUserRequest(
			@NotNull
			String userId,
			@NotNull
			String name,
			@NotNull
			String gender,
			@NotNull
			String birth,
			@NotNull
			String email
	) {

	}
}
