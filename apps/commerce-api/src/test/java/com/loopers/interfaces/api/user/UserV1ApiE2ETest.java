package com.loopers.interfaces.api.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.user.UserV1Dto.RegisterUserRequest;
import com.loopers.interfaces.api.user.UserV1Dto.UserResponse;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserV1ApiE2ETest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	@BeforeEach
	void cleanUp() {
		databaseCleanUp.truncateAllTables();
	}

	@DisplayName("POST /api/v1/users")
	@Nested
	class Register {

		private static final String ENDPOINT = "/api/v1/users";

		@DisplayName("회원 가입이 성공하면, 생성된 유저 정보를 반환한다.")
		@Test
		void returnUserInfo_whenRegisterUserSuccess() {
			// arrange
			RegisterUserRequest request = new RegisterUserRequest(
					"user1",
					"홍길동",
					"M",
					"1990-01-01",
					"foo@example.com"
			);

			// act
			ParameterizedTypeReference<ApiResponse<UserResponse>> responseType = new ParameterizedTypeReference<>() {
			};
			ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(request), responseType);

			// assert
			assertAll(
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
					() -> assertThat(response.getBody()).isNotNull(),
					() -> assertThat(response.getBody().data().userId()).isEqualTo(request.userId()),
					() -> assertThat(response.getBody().data().name()).isEqualTo(request.name()),
					() -> assertThat(response.getBody().data().gender()).isEqualTo(request.gender()),
					() -> assertThat(response.getBody().data().birth()).isEqualTo(request.birth()),
					() -> assertThat(response.getBody().data().email()).isEqualTo(request.email())
			);
		}

		@DisplayName("회원 가입 시 성별이 입력되지 않으면, 400 Bad Request 에러가 발생한다.")
		@Test
		void returnBadRequest_wheGenderIsMissing() {
			// arrange
			RegisterUserRequest request = new RegisterUserRequest(
					"user1",
					"홍길동",
					null,
					"1990-01-01",
					"foo@example.com"
			);

			// act
			ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
			};
			ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(request), responseType);

			// assert
			assertAll(
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
					() -> assertThat(response.getBody().meta().result()).isEqualTo(ApiResponse.Metadata.Result.FAIL),
					() -> assertThat(response.getBody().meta().errorCode()).isEqualTo(ErrorType.BAD_REQUEST.getCode())
			);
		}

		@DisplayName("회원 가입 시 이메일 형식이 틀리면, 400 Bad Request 에러가 발생한다.")
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
		void returnBadRequest_whenEmailIsInvalidFormat(String email) {
			// arrange
			RegisterUserRequest request = new RegisterUserRequest(
					"user1",
					"홍길동",
					"F",
					"1990-01-01",
					email
			);

			// act
			ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
			};
			ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(request), responseType);

			// assert
			assertAll(
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
					() -> assertThat(response.getBody().meta().result()).isEqualTo(ApiResponse.Metadata.Result.FAIL),
					() -> assertThat(response.getBody().meta().errorCode()).isEqualTo(ErrorType.BAD_REQUEST.getCode())
			);
		}

		@DisplayName("회원 가입 시 생년월일의 형식이 yyyy-MM-dd가 아니라면, 400 Bad Request 에러가 발생한다.")
		@ParameterizedTest
		@ValueSource(strings = {
				"",
				"2025-00-00",
				"2025-02-31",
				"111-01-01",
				"2025-3-5",
				"2025.03.05"
		})
		void returnBadRequest_whenBirthIsInvalidFormat(String birth) {
			// arrange
			RegisterUserRequest request = new RegisterUserRequest(
					"user1",
					"홍길동",
					"F",
					birth,
					"foo@example.com"
			);

			// act
			ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
			};
			ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(request), responseType);

			// assert
			assertAll(
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
					() -> assertThat(response.getBody().meta().result()).isEqualTo(ApiResponse.Metadata.Result.FAIL),
					() -> assertThat(response.getBody().meta().errorCode()).isEqualTo(ErrorType.BAD_REQUEST.getCode())
			);
		}

		@DisplayName("ID 가 영문 및 숫자 10자 이내 형식에 맞지 않으면, 400 Bad Request 에러가 발생한다.")
		@ParameterizedTest
		@ValueSource(strings = {
				"", // 최소 글자수를 만족하지 않는 경우
				"asdfg123456", // 최대 글자수를 초과하는 경우
				"홍길동", // 한글이 포함되는 경우
				"USER@example", // 특수문자가 포함되는 경우
		})
		void returnBadRequest_whenUserIdIsInvalidFormat(String userId) {
			// arrange
			RegisterUserRequest request = new RegisterUserRequest(
					userId,
					"홍길동",
					"F",
					"1990-01-01",
					"foo@example.com"
			);

			// act
			ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
			};
			ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(request), responseType);

			// assert
			assertAll(
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
					() -> assertThat(response.getBody().meta().result()).isEqualTo(ApiResponse.Metadata.Result.FAIL),
					() -> assertThat(response.getBody().meta().errorCode()).isEqualTo(ErrorType.BAD_REQUEST.getCode())
			);
		}

		@DisplayName("동일한 userId로 중복 회원가입을 시도한다면, 409 Conflict 에러가 발생한다.")
		@Test
		void returnConflict_whenRegisterWithDuplicatedUserId() {
			// arrange
			RegisterUserRequest request = new RegisterUserRequest(
					"user001",
					"홍길동",
					"F",
					"1990-01-01",
					"foo@example.com"
			);

			ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
			};

			testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(request), responseType);

			// act
			ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response = testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, new HttpEntity<>(request), responseType);

			// assert
			assertAll(
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT),
					() -> assertThat(response.getBody().meta().result()).isEqualTo(ApiResponse.Metadata.Result.FAIL),
					() -> assertThat(response.getBody().meta().errorCode()).isEqualTo(ErrorType.CONFLICT.getCode())
			);
		}
	}

	@DisplayName("GET /api/v1/users/{userId}")
	@Nested
	class GetUser {

		private static final Function<Long, String> ENDPOINT_GET = id -> "/api/v1/users/" + id;

		@DisplayName("회원가입된 사용자의 id로 요청했을 때 생성 응답과 동일한 유저 정보가 반환된다.")
		@Test
		void returnUserInfo_whenUserExists() {
			// arrange
			RegisterUserRequest request = new RegisterUserRequest(
					"user1",
					"홍길동",
					"M",
					"1990-01-01",
					"foo@example.com"
			);

			ParameterizedTypeReference<ApiResponse<UserResponse>> responseType = new ParameterizedTypeReference<>() {
			};

			ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> registerResponse = testRestTemplate.exchange(Register.ENDPOINT, HttpMethod.POST, new HttpEntity<>(request), responseType);
			UserResponse createdUser = registerResponse.getBody().data();

			// act
			ResponseEntity<ApiResponse<UserResponse>> response = testRestTemplate.exchange(ENDPOINT_GET.apply(createdUser.id()), HttpMethod.GET, null, responseType);

			// assert
			assertAll(
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
					() -> assertThat(response.getBody()).isNotNull(),
					() -> assertThat(response.getBody().data()).isEqualTo(createdUser)
			);
		}

		@DisplayName("존재하지 않는 사용자의 정보를 요청하면, 404 Not Found 에러가 발생한다.")
		@Test
		void returnNotFound_whenUserNotExists() {
			// arrange
			Long userId = -999L; // 정상적인 방법으로는 부여되지 않는 ID로 존재하지 않음을 검증한다.

			// act
			ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() {
			};
			ResponseEntity<ApiResponse<UserResponse>> response = testRestTemplate.exchange(ENDPOINT_GET.apply(userId), HttpMethod.GET, null, responseType);

			// assert
			assertAll(
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
					() -> assertThat(response.getBody().meta().result()).isEqualTo(ApiResponse.Metadata.Result.FAIL),
					() -> assertThat(response.getBody().meta().errorCode()).isEqualTo(ErrorType.NOT_FOUND.getCode())
			);
		}
	}
}
