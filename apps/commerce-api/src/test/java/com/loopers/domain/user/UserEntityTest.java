package com.loopers.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserEntityTest {

	@DisplayName("User 모델을 생성할 때,")
	@Nested
	class Create {

		@DisplayName("ID 가 영문 및 숫자 10자 이내 형식에 맞지 않으면, CoreException(BAD_REQUEST)에러가 발생해 User 객체 생성에 실패한다.")
		@ParameterizedTest
		@ValueSource(strings = {
				"", // 최소 글자수를 만족하지 않는 경우
				"asdfg123456", // 최대 글자수를 초과하는 경우
				"홍길동", // 한글이 포함되는 경우
				"USER@example", // 특수문자가 포함되는 경우
		})
		void failWithBadRequest_whenLoginIdIsInvalid(String loginId) {
			// arrange
			final String name = "홍길동";
			final Gender gender = Gender.M;
			final String birth = "1990-01-01";
			final String email = "user@example.com";

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> UserEntity.of(loginId, name, gender, birth, email)
			);

			// assert
			assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
		}

		@DisplayName("이메일 형식이 xx@yy.zz 형식에 맞지 않으면, CoreException(BAD_REQUEST) 에러가 발생해 User 객체 생성에 실패한다.")
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
		void failWithBadRequest_whenUserEmailIsInvalid(String email) {
			// arrange
			final String loginId = "user123456";
			final String name = "홍길동";
			final Gender gender = Gender.M;
			final String birth = "1990-01-01";

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> UserEntity.of(loginId, name, gender, birth, email)
			);

			// assert
			assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
		}

		@DisplayName("생년월일 형식이 yyyy-MM-dd 형식에 맞지 않으면, CoreException(BAD_REQUEST) 에러가 발생해 User 객체 생성에 실패한다.")
		@ParameterizedTest
		@ValueSource(strings = {
				"",
				"2025-00-00",
				"2025-02-31",
				"111-01-01",
				"2025-3-5",
				"2025.03.05"
		})
		void failWithBadRequest_whenUserBirthIsInvalid(String birth) {
			// arrange
			final String loginId = "user123456";
			final String name = "홍길동";
			final Gender gender = Gender.M;
			final String email = "foo@example.com";

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> UserEntity.of(loginId, name, gender, birth, email)
			);

			// assert
			assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
		}
	}
}
