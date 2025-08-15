package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductDetailInfo;
import com.loopers.application.product.ProductSummaryInfo;
import java.time.LocalDate;

public class ProductV1Dto {

	public record ProductDetailResponse(
			Long id,
			String name,
			long price,
			int stock,
			Long brandId,
			String brandName,
			int likeCount
	) {
		public static ProductDetailResponse from(ProductDetailInfo productDetailInfo) {
			return new ProductDetailResponse(
					productDetailInfo.id(),
					productDetailInfo.name(),
					productDetailInfo.price(),
					productDetailInfo.stock(),
					productDetailInfo.brand().id(),
					productDetailInfo.brand().name(),
					productDetailInfo.likeCount()
			);
		}
	}

	public record ProductSummaryResponse(
			Long id,
			String name,
			long price,
			Long brandId,
			String brandName,
			int likeCount,
			LocalDate releaseDate
	) {
		public static ProductSummaryResponse from(ProductSummaryInfo productSummaryInfo) {
			return new ProductSummaryResponse(
					productSummaryInfo.id(),
					productSummaryInfo.name(),
					productSummaryInfo.price(),
					productSummaryInfo.brand().id(),
					productSummaryInfo.brand().name(),
					productSummaryInfo.likeCount(),
					productSummaryInfo.releaseDate()
			);
		}
	}
}
