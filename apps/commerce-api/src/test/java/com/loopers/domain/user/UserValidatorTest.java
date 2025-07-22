package com.loopers.domain.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserValidatorTest {

	@DisplayName("사용자 ID 검증 테스트")
	@Nested
	class UserIdValidation {

		@DisplayName("영문, 숫자 외 다른 문자가 들어간다면, CoreException(BAD_REQUEST) 에러가 발생해 실패한다.")
		@ParameterizedTest
		@ValueSource(strings = {
				"홍길동",
				"foo_",
				" foo ",
				"はい",
				"田中"
		})
		void failWithBadRequest_whenUserIdHasSpecialCharacter(String userId) {
			// arrange

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> UserValidator.validateUserId(userId)
			);

			// assert
			assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
		}

		@DisplayName("제한 길이(1~10)에 맞지 않는 사용자 ID는, CoreException(BAD_REQUEST) 에러가 발생해 실패한다. ")
		@ParameterizedTest
		@ValueSource(strings = {
				"",
				"01234567890"
		})
		void failWithBadRequest_whenUserIdLengthIsInvalid(String userId) {
			// arrange

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> UserValidator.validateUserId(userId)
			);

			// assert
			int userIdLength = userId.length();
			assertThat(userIdLength < 1 || userIdLength > 10).isTrue();
			assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
		}
	}

	@DisplayName("email 형식 검증 테스트")
	@Nested
	class EmailValidation {

		@DisplayName("email (xx@yy.zz)형식이 아니라면, CoreException(BAD_REQUEST) 에러가 발생해 실패한다.")
		@ParameterizedTest
		@ValueSource(strings = {
				"foo",
				"foo@",
				"foo@bar",
				"foo@bar@",
				"foo@bar@baz",
		})
		void failWithBadRequest_whenEmailIsInvalid(String email) {
			// arrange

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> UserValidator.validateEmail(email)
			);

			// assert
			assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
		}
	}

	@DisplayName("birth 형식 검증 테스트")
	@Nested
	class BirthValidation {
		@DisplayName("생년월일이 yyyy-MM-dd 형식이 아니라면, CoreException(BAD_REQUEST) 에러가 발생해 실패한다.")
		@ParameterizedTest
		@ValueSource(strings = {
				"111-01-01",
				"2025-3-5",
				"2025.03.05"
		})
		void failWithBadRequest_whenBirthIsInvalid(String birth) {
			// arrange

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> UserValidator.validateBirth(birth)
			);

			// assert
			assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
		}

		@DisplayName("생년월일이 존재하지 않는 날짜라면, CoreException(BAD_REQUEST) 에러가 발생해 실패한다.")
		@ParameterizedTest
		@ValueSource(strings = {
				"2025-02-29",
				"2025-04-31",
				"2025-02-29T12:34:56"
		})
		void failWithBadRequest_whenBirthIsInvalid_whenDateIsNotExist(String birth) {
			// arrange

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> UserValidator.validateBirth(birth)
			);

			// assert
			assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
		}
	}
}
