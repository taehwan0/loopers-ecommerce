package com.loopers.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest
class UserIntegrationTest {

	@Autowired
	private UserFacade userFacade;

	@Autowired
	private UserService userService;

	@MockitoSpyBean
	private UserRepository userRepository;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}

	@DisplayName("회원 가입 테스트")
	@Nested
	class Register {

		@DisplayName("회원 가입이 성공하면, 생성된 유저 정보를 반환한다.")
		@Test
		void returnUserInfo_whenRegisterUserSuccess() {
			// arrange
			String userId = "user01";
			String name = "홍길동";
			String gender = "M";
			String birth = "1990-01-01";
			String email = "foo@example.com";

			// act
			UserInfo userInfo = userFacade.register(userId, name, gender, birth, email);

			// assert
			assertAll(
					() -> assertThat(userInfo).isNotNull(),
					() -> assertThat(userInfo.id()).isNotNull(),
					() -> assertThat(userInfo.userId()).isEqualTo(userId),
					() -> assertThat(userInfo.name()).isEqualTo(name),
					() -> assertThat(userInfo.gender().toString()).isEqualTo(gender),
					() -> assertThat(userInfo.birth()).isEqualTo(birth),
					() -> assertThat(userInfo.email()).isEqualTo(email),
					() -> verify(userRepository, times(1)).save(any())
			);
		}

		@DisplayName("중복된 userId로 가입을 시도하면 실패한다.")
		@Test
		void fail_whenUserIdIsDuplicated() {
			// arrange
			String userId = "user01";
			String name = "홍길동";
			String gender = "M";
			String birth = "1990-01-01";
			String email = "foo@example.com";

			userFacade.register(userId, name, gender, birth, email);

			// act
			CoreException exception = assertThrows(CoreException.class, () -> userFacade.register(userId, name, gender, birth, email));

			// assert
			assertThat(exception.getErrorType()).isEqualTo(ErrorType.CONFLICT);
		}

		@DisplayName("ID 가 영문 및 숫자 10자 이내 형식에 맞지 않으면, 회원가입에 실패한다.")
		@ParameterizedTest
		@ValueSource(strings = {
				"", // 최소 글자수를 만족하지 않는 경우
				"asdfg123456", // 최대 글자수를 초과하는 경우
				"홍길동", // 한글이 포함되는 경우
				"USER@example", // 특수문자가 포함되는 경우
		})
		void fail_whenUserIdIsInvalid(String userId) {
			// arrange
			final String name = "홍길동";
			final String gender = "M";
			final String birth = "1990-01-01";
			final String email = "user@example.com";

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> userFacade.register(userId, name, gender, birth, email)
			);

			// assert
			assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
		}

		@DisplayName("이메일 형식이 xx@yy.zz 형식에 맞지 않으면, 회원가입에 실패한다.")
		@ParameterizedTest
		@ValueSource(strings = {
				"",
				"useremail",
				"foo@example",
				"foo@bar@example",
				"@example",
				"@example.com",
				"foo@example.c",
				"fo o@example.com",
		})
		void fail_whenUserEmailIsInvalid(String email) {
			// arrange
			final String userId = "user123456";
			final String name = "홍길동";
			final String gender = "M";
			final String birth = "1990-01-01";

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> userFacade.register(userId, name, gender, birth, email)
			);

			// assert
			assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
		}

		@DisplayName("생년월일 형식이 yyyy-MM-dd 형식에 맞지 않으면, 회원가입에 실패한다.")
		@ParameterizedTest
		@ValueSource(strings = {
				"",
				"2025-00-00",
				"2025-02-31",
				"111-01-01",
				"2025-3-5",
				"2025.03.05"
		})
		void fail_whenUserBirthIsInvalid(String birth) {
			// arrange
			final String userId = "user123456";
			final String name = "홍길동";
			final String gender = "M";
			final String email = "foo@example.com";

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> userFacade.register(userId, name, gender, birth, email)
			);

			// assert
			assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
		}
	}

	@DisplayName("정보 조회 테스트")
	@Nested
	class GetUser {

		@DisplayName("해당 ID의 회원이 존재하는 경우, 회원 정보가 반환된다.")
		@Test
		void returnUserInfo_whenUserIsExists() {
			// arrange
			UserInfo existUserInfo = userFacade.register("user01", "홍길동", "M", "1990-01-01", "foo@example.com");

			// act
			UserInfo userInfo = userFacade.getUser(existUserInfo.id());

			// assert
			assertThat(existUserInfo).isEqualTo(userInfo);
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
}
