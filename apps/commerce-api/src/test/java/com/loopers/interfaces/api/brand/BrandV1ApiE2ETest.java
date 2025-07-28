package com.loopers.interfaces.api.brand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.domain.brand.BrandEntity;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.ApiResponse.Metadata.Result;
import com.loopers.interfaces.api.brand.BrandV1Dto.BrandResponse;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.EntityManager;
import java.util.function.Function;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BrandV1ApiE2ETest {

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	BrandJpaRepository brandJpaRepository;

	@Autowired
	EntityManager entityManager;

	@DisplayName("[GET] /api/v1/brands/{brandId}")
	@Nested
	class GetBrandTest {

		static final Function<Long, String> ENDPOINT_GET = id -> "/api/v1/brands/" + id;

		@DisplayName("존재하지 않는 브랜드의 id로 조회를 요청하면, 404 Not Found 에러가 발생한다.")
		@Test
		void returnBadRequest_whenBrandIsNotExists() {
			// arrange
			Long brandId = -999L;
			String endpoint = ENDPOINT_GET.apply(brandId);

			ParameterizedTypeReference<ApiResponse<BrandV1Dto.BrandResponse>> responseType = new ParameterizedTypeReference<>() {};

			// act
			ResponseEntity<ApiResponse<BrandResponse>> response = restTemplate.exchange(endpoint, HttpMethod.GET, null, responseType);

			// assert
			assertAll(
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
					() -> assertThat(response.getBody().meta().result()).isEqualTo(Result.FAIL),
					() -> assertThat(response.getBody().meta().errorCode()).isEqualTo(ErrorType.NOT_FOUND.getCode())
			);
		}

		@DisplayName("존재하는 브랜드의 id로 조회를 요청하면, 해당 브랜드의 정보를 반환한다.")
		@Test
		void returnBrandInfo_whenBrandIsExists() {
			// arrange
			BrandEntity brandEntity = brandJpaRepository.save(new BrandEntity("NIKE", null));

			Long brandId = brandEntity.getId();
			String endpoint = ENDPOINT_GET.apply(brandId);

			ParameterizedTypeReference<ApiResponse<BrandV1Dto.BrandResponse>> responseType = new ParameterizedTypeReference<>() {};

			// act
			ResponseEntity<ApiResponse<BrandResponse>> response = restTemplate.exchange(endpoint, HttpMethod.GET, null, responseType);

			// then
			assertAll(
					() -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
					() -> assertThat(response.getBody().data()).isNotNull(),
					() -> assertThat(response.getBody().data().id()).isEqualTo(brandId)
			);
		}
	}
}
