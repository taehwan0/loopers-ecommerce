package com.loopers.interfaces.api.brand;

import com.loopers.application.brand.BrandFacade;
import com.loopers.application.brand.BrandInfo;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.brand.BrandV1Dto.BrandResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/brands")
@RestController
public class BrandV1ApiController implements BrandV1ApiSpec {

	private final BrandFacade brandFacade;

	@Override
	@GetMapping("/{brandId}")
	public ApiResponse<BrandResponse> getBrand(@PathVariable Long brandId) {
		BrandInfo brandInfo = brandFacade.getBrand(brandId);
		return ApiResponse.success(BrandResponse.from(brandInfo));
	}
}
