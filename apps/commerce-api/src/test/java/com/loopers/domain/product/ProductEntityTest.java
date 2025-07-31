package com.loopers.domain.product;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

public class ProductEntityTest {

	@DisplayName("ProductEntity 생성 테스트")
	@Nested
	class CreateProduct {

		@DisplayName("상품의 이름이 입력되지 않으면, Bad Request를 반환한다.")
		@ParameterizedTest
		@NullAndEmptySource
		void failWithBadRequest_whenProductNameIsNullOrEmpty(String name) {
			// arrange
			final Long brandId = 1L;
			final Price price = Price.of(1000L);
			final Stock stock = Stock.of(10);
			final LocalDate releaseDate = LocalDate.of(2025, 1, 1);

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> ProductEntity.of(name, brandId, price, stock, releaseDate)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
			);
		}

		@DisplayName("상품의 이름은 양끝 공백을 제외하고 255자를 초과하면, Bad Request를 반환한다.")
		@Test
		void failWithBadRequest_whenNameExceedMaxLength() {
			// arrange
			final String name = "a".repeat(256);
			final Long brandId = 1L;
			final Price price = Price.of(1000L);
			final Stock stock = Stock.of(10);
			final LocalDate releaseDate = LocalDate.of(2025, 1, 1);

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> ProductEntity.of(name, brandId, price, stock, releaseDate)
			);

			// assert
			assertAll(
					() -> assertThat(name.length()).isEqualTo(256),
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
			);
		}

		@DisplayName("상품의 이름은 양쪽 공백을 제외하고 255자 이하라면 객체가 정상적으로 생성된다.")
		@Test
		void createProductEntity_whenNameIsLessThanMaxLength() {
			// arrange
			final String name = "  " + "a".repeat(255) + "  ";
			final Long brandId = 1L;
			final Price price = Price.of(1000L);
			final Stock stock = Stock.of(10);
			final LocalDate releaseDate = LocalDate.of(2025, 1, 1);

			// act
			ProductEntity productEntity = ProductEntity.of(name, brandId, price, stock, releaseDate);

			// assert
			assertAll(
					() -> assertThat(productEntity.getName()).isEqualTo(name.trim()),
					() -> assertThat(productEntity.getBrandId()).isEqualTo(brandId),
					() -> assertThat(productEntity.getPrice()).isEqualTo(price),
					() -> assertThat(productEntity.getStock()).isEqualTo(stock),
					() -> assertThat(productEntity.getReleaseDate()).isEqualTo(releaseDate)
			);
		}

		@DisplayName("상품 생성 시 BrandId가 null이면, Bad Request 에러가 발생한다.")
		@Test
		void failWithBadRequest_whenBrandIdIsNull() {
			// arrange
			final String name = "Valid Product";
			final Price price = Price.of(1000L);
			final Stock stock = Stock.of(10);
			final LocalDate releaseDate = LocalDate.of(2025, 1, 1);

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> ProductEntity.of(name, null, price, stock, releaseDate)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
			);
		}

		@DisplayName("상품 생성 시 Price가 null이면, Bad Request 에러가 발생한다.")
		@Test
		void failWithBadRequest_whenPriceIsNull() {
			// arrange
			final String name = "Valid Product";
			final Long brandId = 1L;
			final Stock stock = Stock.of(10);
			final LocalDate releaseDate = LocalDate.of(2025, 1, 1);

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> ProductEntity.of(name, brandId, null, stock, releaseDate)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
			);
		}

		@DisplayName("상품 생성 시 Stock이 null이면, Bad Request 에러가 발생한다.")
		@Test
		void failWithBadRequest_whenStockIsNull() {
			// arrange
			final String name = "Valid Product";
			final Long brandId = 1L;
			final Price price = Price.of(1000L);
			final LocalDate releaseDate = LocalDate.of(2025, 1, 1);

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> ProductEntity.of(name, brandId, price, null, releaseDate)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
			);
		}

		@DisplayName("상품 생성 시 ReleaseDate가 null이면, Bad Request 에러가 발생한다.")
		@Test
		void failWithBadRequest_whenReleaseDateIsNull() {
			// arrange
			final String name = "Valid Product";
			final Long brandId = 1L;
			final Price price = Price.of(1000L);
			final Stock stock = Stock.of(10);

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> ProductEntity.of(name, brandId, price, stock, null)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
			);
		}
	}
}
