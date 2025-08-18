package com.loopers.domain.product;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductQueryService {

	private final ProductQueryRepository productQueryRepository;

	// TODO: condition vo로 변경 필요함!
	public Page<ProductSummary> getProductSummaries(
			Long brandId,
			ProductSummarySort sortBy,
			int page,
			int size
	) {
		List<ProductSummary> products = productQueryRepository.getProductSummariesWithBrandFilter(brandId, sortBy, page, size);
		long count;

		if (brandId != null) {
			count = productQueryRepository.getTotalCountWithBrandFilter(brandId);
		} else {
			count = productQueryRepository.getTotalCount();
		}

		return new PageImpl<>(
				products,
				Pageable.ofSize(size).withPage(page),
				count
		);
	}
}
