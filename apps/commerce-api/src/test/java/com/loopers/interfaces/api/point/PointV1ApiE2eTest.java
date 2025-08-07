package com.loopers.interfaces.api.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserEntity;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.point.PointV1Dto.ChargePointRequest;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PointV1ApiE2eTest {

	@Autowired
	TestRestTemplate testRestTemplate;

	@Autowired
	UserJpaRepository userJpaRepository;

	@Autowired
	DatabaseCleanUp databaseCleanUp;

	UserEntity createUser() {
		UserEntity user = UserEntity.of(
				"USER123",
				"홍길동",
				Gender.M,
				"1990-01-01",
				"foo@example.com"
		);

		return userJpaRepository.save(user);
	}

	@AfterEach
	void cleanUp() {
		databaseCleanUp.truncateAllTables();
	}

	@DisplayName("GET /api/v1/points")
	@Nested
	class GetUserPoints {

		static final String ENDPOINT_GET_POINTS = "/api/v1/points";
		static final String X_USER_ID_HEADER = "X-USER-ID";

		@DisplayName("X-USER-ID 헤더가 없으면, 400 Bad Request 에러가 발생한다.")
		@Test
		void returnBadRequest_whenHeaderUserIdIsMissing() {
			// arrange

			// act
			ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
			};
			ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response = testRestTemplate.exchange(ENDPOINT_GET_POINTS, HttpMethod.GET, null, responseType);

			// assert
			assertAll(
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
					() -> assertThat(response.getBody().meta().result()).isEqualTo(ApiResponse.Metadata.Result.FAIL),
					() -> assertThat(response.getBody().meta().errorCode()).isEqualTo(ErrorType.BAD_REQUEST.getCode())
			);
		}

		@DisplayName("존재하지 않는 사용자의 정보를 요청하면, 404 Not Found 에러가 발생한다.")
		@Test
		void returnNotFound_whenUserNotExists() {
			// arrange
			String unknownUserId = "unknown";
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set(X_USER_ID_HEADER, unknownUserId);

			// act
			ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
			};
			ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response = testRestTemplate.exchange(ENDPOINT_GET_POINTS, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType);

			// assert
			assertAll(
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
					() -> assertThat(response.getBody().meta().result()).isEqualTo(ApiResponse.Metadata.Result.FAIL),
					() -> assertThat(response.getBody().meta().errorCode()).isEqualTo(ErrorType.NOT_FOUND.getCode())
			);
		}

		@DisplayName("존재하는 사용자의 정보를 요청하면, 해당 사용자의 포인트 정보를 반환한다.")
		@Test
		void returnUserPoints_whenUserExists() {
			// arrange
			UserEntity user = createUser();
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set(X_USER_ID_HEADER, user.getLoginId());

			// act
			ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
			};
			ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response = testRestTemplate.exchange(ENDPOINT_GET_POINTS, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType);

			// assert
			assertAll(
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
					() -> assertThat(response.getBody()).isNotNull(),
					() -> assertThat(response.getBody().data().pointValue()).isGreaterThanOrEqualTo(0)
			);
		}
	}

	@DisplayName("POST /api/v1/points/charge")
	@Nested
	class ChargePoints {

		static final String ENDPOINT_CHARGE_POINTS = "/api/v1/points/charge";
		static final String X_USER_ID_HEADER = "X-USER-ID";

		@DisplayName("X-USER-ID 헤더가 없으면, 400 Bad Request 에러가 발생한다.")
		@Test
		void returnBadRequest_whenHeaderUserIdIsMissing() {
			// arrange

			// act
			ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
			};
			ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response = testRestTemplate.exchange(ENDPOINT_CHARGE_POINTS, HttpMethod.POST, null, responseType);

			// assert
			assertAll(
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
					() -> assertThat(response.getBody().meta().result()).isEqualTo(ApiResponse.Metadata.Result.FAIL),
					() -> assertThat(response.getBody().meta().errorCode()).isEqualTo(ErrorType.BAD_REQUEST.getCode())
			);
		}

		@DisplayName("존재하지 않는 대상이 포인트 충전을 요청하면, 404 Not Found 에러가 발생한다.")
		@Test
		void returnNotFound_whenUnknownUsersRequest() {
			// arrange
			String unknownUserId = "unknownUser";

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set(X_USER_ID_HEADER, unknownUserId);
			ChargePointRequest chargePointRequest = new ChargePointRequest(1000);
			HttpEntity<ChargePointRequest> requestHttpEntity = new HttpEntity<>(chargePointRequest, httpHeaders);

			// act
			ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
			};
			ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response = testRestTemplate.exchange(ENDPOINT_CHARGE_POINTS, HttpMethod.POST, requestHttpEntity, responseType);

			// assert
			assertAll(
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
					() -> assertThat(response.getBody().meta().result()).isEqualTo(ApiResponse.Metadata.Result.FAIL),
					() -> assertThat(response.getBody().meta().errorCode()).isEqualTo(ErrorType.NOT_FOUND.getCode())
			);
		}

		@DisplayName("포인트 충전을 요청이 성공하면, 충전된 잔액을 반환한다.")
		@Test
		void returnTotalPoint_whenChargePointSuccessful() {
			// arrange
			UserEntity user = createUser();
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set(X_USER_ID_HEADER, user.getLoginId());
			ChargePointRequest chargePointRequest = new ChargePointRequest(1000);
			HttpEntity<ChargePointRequest> requestHttpEntity = new HttpEntity<>(chargePointRequest, httpHeaders);

			// act
			ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
			};
			ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> firstResponse = testRestTemplate.exchange(ENDPOINT_CHARGE_POINTS, HttpMethod.POST, requestHttpEntity, responseType);
			ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> secondResponse = testRestTemplate.exchange(ENDPOINT_CHARGE_POINTS, HttpMethod.POST, requestHttpEntity, responseType);

			// assert
			assertAll(
					() -> assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.OK),
					() -> assertThat(firstResponse.getBody()).isNotNull(),
					() -> assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.OK),
					() -> assertThat(firstResponse.getBody()).isNotNull(),
					() -> assertThat(secondResponse.getBody().data().pointValue()).isEqualTo(firstResponse.getBody().data().pointValue() + 1000)
			);
		}

		@DisplayName("0이하의 포인트를 충전하려고 하면, 400 Bad Request 에러가 발생한다.")
		@ParameterizedTest
		@ValueSource(ints = {Integer.MIN_VALUE, -1, 0})
		void returnBadRequest_whenChargePointIsLessThanZero(int amount) {
			// arrange
			UserEntity user = createUser();
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.set(X_USER_ID_HEADER, user.getLoginId());
			ChargePointRequest chargePointRequest = new ChargePointRequest(amount);
			HttpEntity<ChargePointRequest> requestHttpEntity = new HttpEntity<>(chargePointRequest, httpHeaders);

			// act
			ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
			};
			ResponseEntity<ApiResponse<PointV1Dto.PointResponse>> response = testRestTemplate.exchange(ENDPOINT_CHARGE_POINTS, HttpMethod.POST, requestHttpEntity, responseType);

			// assert
			assertAll(
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST),
					() -> assertThat(response.getBody().meta().result()).isEqualTo(ApiResponse.Metadata.Result.FAIL),
					() -> assertThat(response.getBody().meta().errorCode()).isEqualTo(ErrorType.BAD_REQUEST.getCode())
			);
		}
	}
}
