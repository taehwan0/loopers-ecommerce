package com.loopers.domain.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.application.point.PointFacade;
import com.loopers.application.point.PointInfo;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PointIntegrationTest {

	@Autowired
	PointFacade pointFacade;

	@Autowired
	UserJpaRepository userJpaRepository;
	@Autowired
	private UserService userService;

	UserEntity createUser() {
		UserEntity user = UserEntity.of(
				"USER",
				"홍길동",
				Gender.M,
				"2020-01-01",
				"foo@example.com"
		);

		return userJpaRepository.save(user);
	}

	@DisplayName("포인트 조회 테스트")
	@Nested
	class GetPoint {

		@DisplayName("해당 ID의 회원이 존재하는 경우, 포인트 정보가 반환된다.")
		@Test
		void returnPoint_whenUserIsExists() {
			// arrange
			UserEntity user = createUser();

			// act
			PointInfo pointInfo = pointFacade.getUserPoint(user.getLoginId());

			// assert
			assertAll(
					() -> assertThat(pointInfo).isNotNull(),
					() -> assertThat(pointInfo.pointValue()).isEqualTo(0)
			);
		}

		@DisplayName("해당 ID의 회원이 존재하지 않는 경우, Null이 반환된다.")
		@Test
		void returnNull_whenUserIsNotFound() {
			// arrange
			Long userId = -999L;

			// act
			Optional<UserEntity> user = userService.getUser(userId);

			// assert
			assertThat(user.isEmpty()).isTrue();
		}
	}

	@DisplayName("포인트 충전 테스트")
	@Nested
	class ChargePoint {

		@DisplayName("존재하지 않는 사용자에 포인트를 충전하면, CoreException(NOT_FOUND) 에러가 발생한다.")
		@Test
		void failWithNotFound_whenUserIsNotFound() {
			// arrange
			String loginId = "notfound";
			int amount = 1000;

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> pointFacade.chargePoint(loginId, amount)
			);

			// assert
			assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
		}
	}
}
