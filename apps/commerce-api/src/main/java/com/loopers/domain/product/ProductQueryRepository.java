package com.loopers.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductQueryRepository {
	Optional<ProductSummary> getProductSummary(Long productId);

	List<ProductSummary> getProductSummariesWithBrandFilter(
			Long brandId,
			ProductSummarySort sortBy,
			int page,
			int size
	);

	long getTotalCount();

	long getTotalCountWithBrandFilter(Long brandId);
}
