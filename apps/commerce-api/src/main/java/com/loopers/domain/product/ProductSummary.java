package com.loopers.domain.product;

import com.loopers.domain.vo.Price;
import java.time.LocalDate;

public record ProductSummary(
		Long id,
		String name,
		Price price,
		LocalDate releaseDate,
		BrandSummary brand,
		int likeCount
) {

	public record BrandSummary(
			Long id,
			String name
	) {

	}
}
