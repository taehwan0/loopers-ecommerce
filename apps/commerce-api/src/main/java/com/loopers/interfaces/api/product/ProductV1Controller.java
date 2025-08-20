package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductDetailInfo;
import com.loopers.application.product.ProductFacade;
import com.loopers.application.product.ProductSummariesCommand;
import com.loopers.application.product.ProductSummariesCommand.SortBy;
import com.loopers.application.product.ProductSummaryInfo;
import com.loopers.application.shared.PageInfo;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.PageResponse;
import com.loopers.interfaces.api.PageResponse.PageMeta;
import com.loopers.interfaces.api.product.ProductV1Dto.ProductDetailResponse;
import com.loopers.interfaces.api.product.ProductV1Dto.ProductSummaryResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@RestController
public class ProductV1Controller implements ProductV1ApiSpec {

	private final ProductFacade productFacade;

	@GetMapping("/{productId}")
	@Override
	public ApiResponse<ProductDetailResponse> getProduct(
			@PathVariable(value = "productId") Long productId
	) {
		ProductDetailInfo productDetail = productFacade.getProductDetail(productId);

		return ApiResponse.success(ProductDetailResponse.from(productDetail));
	}

	@GetMapping("")
	@Override
	public ApiResponse<PageResponse<ProductSummaryResponse>> getProducts(
			@RequestParam(value = "brandId", required = false) Long brandId,
			@RequestParam(value = "sortBy", required = false, defaultValue = "LATEST") String sortBy,
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "size", required = false, defaultValue = "10") int size
	) {
		ProductSummariesCommand command = ProductSummariesCommand.of(brandId, SortBy.from(sortBy), page, size);

		PageInfo<ProductSummaryInfo> productSummaries = productFacade.getProductSummaries(command);

		List<ProductSummaryResponse> products = productSummaries.content().stream().map(ProductSummaryResponse::from).toList();

		PageMeta pageMeta = PageMeta.of(
				productSummaries.page(),
				productSummaries.size(),
				productSummaries.totalElements(),
				productSummaries.totalPages()
		);

		PageResponse<ProductSummaryResponse> pageResponse = PageResponse.from(products, pageMeta);

		return ApiResponse.success(pageResponse);
	}
}
