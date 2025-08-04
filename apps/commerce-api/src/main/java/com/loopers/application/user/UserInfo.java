package com.loopers.application.user;

import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserEntity;
import java.time.LocalDate;

public record UserInfo(
		Long id,
		String loginId,
		String name,
		Gender gender,
		LocalDate birth,
		String email
) {

	static UserInfo from(UserEntity entity) {
		return new UserInfo(
				entity.getId(),
				entity.getLoginId(),
				entity.getName(),
				entity.getGender(),
				entity.getBirth(),
				entity.getEmail()
		);
	}
}
