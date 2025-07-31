package com.loopers.application.product;

import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.like.LikeCountEntity;
import com.loopers.domain.product.ProductEntity;

public record ProductDetailInfo(
		Long id,
		String name,
		long price,
		int stock,
		BrandSummary brand,
		int likeCount
) {
	public static ProductDetailInfo from(ProductEntity product, BrandEntity brand, LikeCountEntity likeCount) {
		return new ProductDetailInfo(
				product.getId(),
				product.getName(),
				product.getPrice().getAmount(),
				product.getStock().getQuantity(),
				BrandSummary.from(brand),
				likeCount.getLikeCount()
		);
	}

	public record BrandSummary(Long id, String name) {
		public static BrandSummary from(BrandEntity brand) {
			return new BrandSummary(brand.getId(), brand.getName());
		}
	}
}
