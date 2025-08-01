package com.loopers.application.product;

import com.loopers.domain.product.ProductSummary;
import java.time.LocalDate;

public record ProductSummaryInfo(
		Long id,
		String name,
		long price,
		BrandSummary brand,
		int likeCount,
		LocalDate releaseDate
) {

	public static ProductSummaryInfo from(ProductSummary productSummary) {
		return new ProductSummaryInfo(
				productSummary.id(),
				productSummary.name(),
				productSummary.price().getAmount(),
				new BrandSummary(productSummary.brand().id(), productSummary.brand().name()),
				productSummary.likeCount(),
				productSummary.releaseDate()
		);
	}
}
