package com.loopers.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.application.product.ProductDetailInfo;
import com.loopers.application.product.ProductFacade;
import com.loopers.domain.brand.BrandEntity;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductIntegrationTest {

	private static final String REGISTERED_BRAND_NAME = "NIKE";
	private static final String REGISTERED_PRODUCT_ID = "SHOES-001";

	@Autowired
	ProductFacade productFacade;

	@Autowired
	ProductJpaRepository productJpaRepository;

	@Autowired
	BrandJpaRepository brandJpaRepository;

	ProductEntity createProduct() {
		BrandEntity brand = brandJpaRepository.save(BrandEntity.of(REGISTERED_BRAND_NAME, "description"));

		ProductEntity product = ProductEntity.of(
				REGISTERED_PRODUCT_ID,
				brand.getId(),
				Price.of(1000L),
				Stock.of(10)
		);

		return productJpaRepository.save(product);
	}

	@DisplayName("상품 조회 테스트")
	@Nested
	class GetProduct {

		@DisplayName("존재하지 않는 상품 ID로 조회하면, Not Found 에러가 발생해 실패한다.")
		@Test
		void failWithNotFound_whenProductIsNotExists() {
			// arrange
			Long productId = -999L;

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> productFacade.getProductDetail(productId)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND)
			);
		}

		@DisplayName("존재하는 상품을 조회하면, 브랜드 정보와 좋아요 수를 포함해 반환한다.")
		@Test
		void returnProductDetailWithBrandAndLikeCount_whenProductIsExists() {
			// arrange
			ProductEntity product = createProduct();
			Long productId = product.getId();

			// act
			ProductDetailInfo productDetailInfo = productFacade.getProductDetail(productId);

			// assert
			assertAll(
					() -> assertThat(productDetailInfo).isNotNull(),
					() -> assertThat(productDetailInfo.brand()).isNotNull(),
					() -> assertThat(productDetailInfo.likeCount()).isGreaterThanOrEqualTo(0)
			);
		}
	}
}
