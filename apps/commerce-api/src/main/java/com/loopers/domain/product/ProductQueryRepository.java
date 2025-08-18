package com.loopers.domain.product;

import java.util.List;

public interface ProductQueryRepository {
	List<ProductSummary> getProductSummariesWithBrandFilter(
			Long brandId,
			ProductSummarySort sortBy,
			int page,
			int size
	);

	long getTotalCount();

	long getTotalCountWithBrandFilter(Long brandId);
}
