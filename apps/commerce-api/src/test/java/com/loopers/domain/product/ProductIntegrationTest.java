package com.loopers.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.application.product.ProductDetailInfo;
import com.loopers.application.product.ProductFacade;
import com.loopers.application.product.ProductSummariesCommand;
import com.loopers.application.product.ProductSummariesCommand.SortBy;
import com.loopers.application.product.ProductSummaryInfo;
import com.loopers.application.shared.PageInfo;
import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.like.LikeCountEntity;
import com.loopers.domain.like.LikeTarget;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.like.LikeCountJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductIntegrationTest {

	@Autowired
	ProductFacade productFacade;

	@Autowired
	ProductJpaRepository productJpaRepository;

	@Autowired
	BrandJpaRepository brandJpaRepository;

	@Autowired
	LikeCountJpaRepository likeCountJpaRepository;

	@Autowired
	DatabaseCleanUp databaseCleanUp;

	@AfterEach
	void cleanUp() {
		databaseCleanUp.truncateAllTables();
	}

	@DisplayName("상품 단일 조회 테스트")
	@Nested
	class GetProduct {

		static final String REGISTERED_BRAND_NAME = "NIKE";
		static final String REGISTERED_PRODUCT_ID = "SHOES-001";

		ProductEntity createProduct() {
			BrandEntity brand = brandJpaRepository.save(BrandEntity.of(REGISTERED_BRAND_NAME, "description"));

			ProductEntity product = ProductEntity.of(
					REGISTERED_PRODUCT_ID,
					brand.getId(),
					Price.of(1000L),
					Stock.of(10),
					LocalDate.of(2025, 1, 1)
			);

			return productJpaRepository.save(product);
		}

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

	@DisplayName("상품 리스트 조회 테스트")
	@Nested
	class GetProducts {

		static final String REGISTERED_BRAND_NAME = "NIKE";

		void createProducts() {
			BrandEntity brand = brandJpaRepository.save(BrandEntity.of(REGISTERED_BRAND_NAME, "description"));

			List<ProductEntity> products = List.of(
					ProductEntity.of(
							"SHOES-001",
							brand.getId(),
							Price.of(1000L),
							Stock.of(10),
							LocalDate.of(2025, 1, 1)
					),
					ProductEntity.of(
							"SHOES-002",
							brand.getId(),
							Price.of(3000L),
							Stock.of(10),
							LocalDate.of(2025, 1, 2)
					),
					ProductEntity.of(
							"SHOES-003",
							brand.getId(),
							Price.of(2000L),
							Stock.of(10),
							LocalDate.of(2025, 1, 3)
					)
			);

			productJpaRepository.saveAll(products);

			List<LikeCountEntity> likesCount = products.stream()
					.map(p -> {
						LikeCountEntity likeCount = LikeCountEntity.of(LikeTarget.of(p.getId(), LikeTargetType.PRODUCT));
						for (int i = 0; i < likeCount.getId(); i++) {
							likeCount.increaseLikeCount();
						}
						return likeCount;
					})
					.toList();

			likeCountJpaRepository.saveAll(likesCount);
		}

		@DisplayName("상품 리스트 조회 시 가격 정렬 조건에 맞게 정렬되어 반환된다.")
		@Test
		void returnProductListSortedByPrice_whenSortIsPrice() {
			// arrange
			createProducts();
			ProductSummariesCommand command = ProductSummariesCommand.of(SortBy.PRICE_ASC, 0, 10);

			// act
			PageInfo<ProductSummaryInfo> productSummaries = productFacade.getProductSummaries(command);

			// assert
			assertAll(
					() -> assertThat(productSummaries).isNotNull(),
					() -> assertThat(productSummaries.content()).isNotEmpty(),
					() -> assertThat(productSummaries.content().get(0).price()).isLessThanOrEqualTo(productSummaries.content().get(1).price()),
					() -> assertThat(productSummaries.content().get(1).price()).isLessThanOrEqualTo(productSummaries.content().get(2).price())
			);

		}

		@DisplayName("상품 리스트 조회 시 좋아요 순 조건에 맞게 정렬되어 반환된다.")
		@Test
		void returnProductListSortedByLikeCount_whenSortIsLikeCount() {
			// arrange
			createProducts();
			ProductSummariesCommand command = ProductSummariesCommand.of(SortBy.LIKES_DESC, 0, 10);

			// act
			PageInfo<ProductSummaryInfo> productSummaries = productFacade.getProductSummaries(command);

			// assert
			assertAll(
					() -> assertThat(productSummaries).isNotNull(),
					() -> assertThat(productSummaries.content()).isNotEmpty(),
					() -> assertThat(productSummaries.content().get(0).likeCount()).isGreaterThanOrEqualTo(productSummaries.content().get(1).likeCount()),
					() -> assertThat(productSummaries.content().get(1).likeCount()).isGreaterThanOrEqualTo(productSummaries.content().get(2).likeCount())
			);
		}

		@DisplayName("상품 리스트 조회 시 최신 정렬 조건에 맞게 정렬되어 반환된다.")
		@Test
		void returnProductListSortedByLatest_whenSortIsLatest() {
			// arrange
			createProducts();
			ProductSummariesCommand command = ProductSummariesCommand.of(SortBy.LATEST, 0, 10);

			// act
			PageInfo<ProductSummaryInfo> productSummaries = productFacade.getProductSummaries(command);

			// assert
			assertAll(
					() -> assertThat(productSummaries).isNotNull(),
					() -> assertThat(productSummaries.content()).isNotEmpty(),
					() -> assertThat(productSummaries.content().get(0).releaseDate()).isAfterOrEqualTo(productSummaries.content().get(1).releaseDate()),
					() -> assertThat(productSummaries.content().get(1).releaseDate()).isAfterOrEqualTo(productSummaries.content().get(2).releaseDate())
			);
		}

		@DisplayName("상품 리스트 조회 시 페이징이 오버된다면, 빈 배열이 반환된다.")
		@Test
		void returnEmptyList_whenPageIsOver() {
			// arrange
			int page = 100;
			int size = 10;
			ProductSummariesCommand command = ProductSummariesCommand.of(SortBy.LATEST, page, size);

			// act
			PageInfo<ProductSummaryInfo> productSummaries = productFacade.getProductSummaries(command);

			// assert
			assertAll(
					() -> assertThat(productSummaries).isNotNull(),
					() -> assertThat(productSummaries.content()).isEmpty(),
					() -> assertThat(productSummaries.page()).isEqualTo(page),
					() -> assertThat(productSummaries.size()).isEqualTo(size)
			);
		}
	}
}
