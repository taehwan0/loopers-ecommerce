package com.loopers.application.brand;

import com.loopers.domain.brand.BrandEntity;

public record BrandInfo(
		Long id,
		String name,
		String description
) {
	public static BrandInfo from(BrandEntity entity) {
		return new BrandInfo(
				entity.getId(),
				entity.getName(),
				entity.getDescription()
		);
	}
}
