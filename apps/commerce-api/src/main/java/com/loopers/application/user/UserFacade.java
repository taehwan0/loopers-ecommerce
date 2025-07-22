package com.loopers.application.user;


import com.loopers.domain.user.Gender;
import com.loopers.domain.user.Point;
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

	@Transactional
	public UserInfo register(String userId, String name, String gender, String birth, String email) {
		UserValidator.validateBeforeCreateUser(userId, birth, email);

		if (userService.findByUerId(userId).isEmpty()) {
			UserEntity userEntity = userService.create(userId, name, Gender.of(gender), birth, email);
			return UserInfo.from(userEntity);
		}

		throw new CoreException(ErrorType.CONFLICT, "이미 사용중인 ID 입니다.");
	}

	@Transactional(readOnly = true)
	public UserInfo getUser(Long id) {
		return userService.getUser(id)
				.map(UserInfo::from)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[id = " + id + "] 사용자를 찾을 수 없습니다."));
	}

	@Transactional(readOnly = true)
	public PointInfo getUserPoint(String userId) {
		UserEntity user = userService.findByUerId(userId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[id = " + userId + "] 사용자를 찾을 수 없습니다."));

		Point point = user.getPoint();

		return PointInfo.from(point);
	}

	@Transactional
	public PointInfo chargePoint(String userId, int amount) {
		UserEntity userEntity = userService.findByUerId(userId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[userId = " + userId + "] 사용자를 찾을 수 없습니다."));

		userEntity.chargePoint(amount);

		Point point = userEntity.getPoint();

		return PointInfo.from(point);
	}
}
