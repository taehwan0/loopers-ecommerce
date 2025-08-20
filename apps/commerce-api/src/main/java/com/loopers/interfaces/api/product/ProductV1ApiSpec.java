package com.loopers.interfaces.api.product;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.PageResponse;
import com.loopers.interfaces.api.product.ProductV1Dto.ProductDetailResponse;
import com.loopers.interfaces.api.product.ProductV1Dto.ProductSummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product V1 API", description = "Loopers 상품 API")
public interface ProductV1ApiSpec {

	@Operation(
			summary = "상품 조회",
			description = "상품을 조회합니다."
	)
	ApiResponse<ProductDetailResponse> getProduct(Long productId);

	@Operation(
			summary = "상품 리스트 조회",
			description = "상품 리스트를 조회합니다."
	)
	ApiResponse<PageResponse<ProductSummaryResponse>> getProducts(
			Long brandId,
			String sortBy,
			int page,
			int size
	);
}
