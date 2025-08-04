package com.loopers.domain.brand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.application.brand.BrandFacade;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BrandIntegrationTest {

	@Autowired
	BrandFacade brandFacade;

	@DisplayName("brand 조회 테스트")
	@Nested
	class GetBrand {

		@DisplayName("존재하지 않는 id로 조회한다면, Not Found 에러가 발생한다.")
		@Test
		void failWithNotFound_whenBrandIdIsNotExists() {
			// arrange
			Long brandId = -999L;

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> brandFacade.getBrand(brandId)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND)
			);
		}
	}
}
