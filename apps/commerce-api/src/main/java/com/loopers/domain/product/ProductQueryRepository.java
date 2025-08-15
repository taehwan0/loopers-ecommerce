package com.loopers.domain.product;

import org.springframework.data.domain.Page;

public interface ProductQueryRepository {
	Page<ProductSummary> getProductSummaries(
			Long brandId,
			ProductSummarySort sortBy,
			int page,
			int size
	);
}
