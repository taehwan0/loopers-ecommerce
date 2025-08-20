package com.loopers.application.product;

import com.loopers.application.shared.PageInfo;
import com.loopers.domain.product.ProductQueryService;
import com.loopers.domain.product.ProductSummary;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductFacade {

	private final ProductQueryService productQueryService;

	@Transactional
	public ProductDetailInfo getProductDetail(Long productId) {
		ProductSummary product = productQueryService.getProductSummary(productId);
		return new ProductDetailInfo(
				product.id(),
				product.name(),
				product.price().getAmount(),
				0,
				new BrandSummary(product.brandId(), product.brandName()),
				product.likeCount()
		);
	}

	@Transactional
	public PageInfo<ProductSummaryInfo> getProductSummaries(ProductSummariesCommand query) {
		Page<ProductSummary> productSummariesPage = productQueryService.getProductSummaries(
				query.brandId(),
				query.sortBy().toProductSummarySort(),
				query.page(),
				query.size()
		);

		List<ProductSummaryInfo> content = productSummariesPage.getContent()
				.stream()
				.map(ProductSummaryInfo::from)
				.toList();

		return PageInfo.of(
				content,
				productSummariesPage.getNumber(),
				productSummariesPage.getSize(),
				productSummariesPage.getTotalElements(),
				productSummariesPage.getTotalPages()
		);
	}
}
