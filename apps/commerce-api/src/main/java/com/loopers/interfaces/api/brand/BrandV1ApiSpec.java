package com.loopers.interfaces.api.brand;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.brand.BrandV1Dto.BrandResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Brand V1 API", description = "Loopers Brand API")
public interface BrandV1ApiSpec {

	@Operation(
			summary = "브랜드 조회",
			description = "ID로 브랜드를 조회합니다."
	)
	ApiResponse<BrandResponse> getBrand(Long brandId);
}
