package com.loopers.domain.brand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

public class BrandEntityTest {

	@DisplayName("BrandEntity 생성 테스트")
	@Nested
	class CreateBrandEntity {

		@DisplayName("브랜드의 이름이 입력되지 않으면, Bad Request를 반환한다.")
		@ParameterizedTest
		@NullAndEmptySource
		void failWithBadRequest_whenBrandNameIsNullOrEmpty(String name) {
			// arrange
			final String description = "description";

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> new BrandEntity(name, description)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
			);
		}

		@DisplayName("브랜드의 이름은 양끝 공백을 제외하고 255자를 초과하면, Bad Request를 반환한다.")
		@Test
		void failWithBadReqeust_whenNameExceedMaxLength() {
			// arragnge
			final String name = "a".repeat(256);
			final String description = "description";

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> new BrandEntity(name, description)
			);

			// assert
			assertAll(
					() -> assertThat(name.length()).isEqualTo(256),
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
			);
		}

		@DisplayName("브랜드의 이름은 양끝 공백을 제외하고 255자이하라면 객체가 정상적으로 생성된다.")
		@Test
		void createBrandEntity_whenNameIsLessThanMaxLength() {
			// arragnge
			final String name = "  " + "a".repeat(255) + "  ";
			final String description = "description";

			// act
			BrandEntity brandEntity = new BrandEntity(name, description);

			// assert
			assertAll(
					() -> assertThat(brandEntity).isNotNull(),
					() -> assertThat(brandEntity.getName()).isEqualTo(name.trim()),
					() -> assertThat(brandEntity.getName().length()).isEqualTo(255)
			);
		}
	}
}
