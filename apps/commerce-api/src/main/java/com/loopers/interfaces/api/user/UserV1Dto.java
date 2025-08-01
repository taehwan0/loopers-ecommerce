package com.loopers.interfaces.api.user;

import com.loopers.application.user.PointInfo;
import com.loopers.application.user.UserInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

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

	public record UserPointResponse(long pointValue) {
		public static UserPointResponse from(PointInfo pointInfo) {
			return new UserPointResponse(pointInfo.pointValue());
		}
	}

	public record RegisterUserRequest(
			@Pattern(regexp = "^[a-zA-Z0-9]{1,10}$")
			String userId,
			@NotNull
			String name,
			@NotNull
			String gender,
			@Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$")
			String birth,
			@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
			String email
	) {

	}


	public record ChargePointRequest(
			@Min(value = 1, message = "포인트는 1 이상이어야 합니다.")
			int amount
	) {

	}
}
