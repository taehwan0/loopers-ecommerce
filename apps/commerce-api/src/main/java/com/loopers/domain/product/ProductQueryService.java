package com.loopers.domain.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductQueryService {

	private final ProductQueryRepository productQueryRepository;

	public Page<ProductSummary> getProductSummaries(
		ProductSummarySort sortBy,
		int page,
		int size
	) {
		return productQueryRepository.getProductSummaries(sortBy, page, size);
	}
}
