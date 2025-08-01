package com.loopers.domain.product;

import org.springframework.data.domain.Page;

public interface ProductQueryRepository {
	Page<ProductSummary> getProductSummaries(
			ProductSummarySort sortBy,
			int page,
			int size
	);
}
