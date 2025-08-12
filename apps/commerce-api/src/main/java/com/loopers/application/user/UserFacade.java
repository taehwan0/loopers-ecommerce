package com.loopers.application.user;


import com.loopers.domain.point.PointService;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
import com.loopers.domain.user.UserValidator;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserFacade {

	private final UserService userService;
	private final PointService pointService;

	@Transactional
	public UserInfo register(String loginId, String name, String gender, String birth, String email) {
		UserValidator.validateBeforeCreateUser(loginId, birth, email);

		if (isExistLoginId(loginId)) {
			throw new CoreException(ErrorType.CONFLICT, "이미 사용중인 ID 입니다.");
		}

		UserEntity userEntity = userService.create(loginId, name, Gender.of(gender), birth, email);
		pointService.createPointAccount(userEntity.getId());

		return UserInfo.from(userEntity);
	}

	private boolean isExistLoginId(String loginId) {
		return userService.findByLoginId(loginId).isPresent();
	}

	@Transactional(readOnly = true)
	public UserInfo getUser(Long id) {
		return userService.getUser(id)
				.map(UserInfo::from)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[id = " + id + "] 사용자를 찾을 수 없습니다."));
	}
}
