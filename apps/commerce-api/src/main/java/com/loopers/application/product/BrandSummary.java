package com.loopers.application.product;

import com.loopers.domain.brand.BrandEntity;

public record BrandSummary(Long id, String name) {

	public static BrandSummary from(BrandEntity brand) {
		return new BrandSummary(brand.getId(), brand.getName());
	}
}
