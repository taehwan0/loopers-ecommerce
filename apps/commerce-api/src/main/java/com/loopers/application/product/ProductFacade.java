package com.loopers.application.product;

import com.loopers.application.shared.PageInfo;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.LikeCountEntity;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.ProductQueryService;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.product.ProductSummary;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductFacade {

	private final ProductService productService;
	private final BrandService brandService;
	private final LikeService likeService;
	private final ProductQueryService productQueryService;
	private final RedisTemplate<String, Object> redisTemplate;

	@Transactional
	public ProductDetailInfo getProductDetail(Long productId) {

		final String key = "productDetail:" + productId;
		Object object = redisTemplate.opsForValue().get(key);
		if (object != null) {
			return (ProductDetailInfo) object;
		}

		var product = productService.getProduct(productId);

		var brand = brandService.getBrand(product.getBrandId())
				.orElseThrow(
						() -> new CoreException(ErrorType.NOT_FOUND, "[brandId = " + product.getBrandId() + "] 브랜드를 찾을 수 없습니다."));

		LikeCountEntity likeCount = likeService.getProductLikeCount(productId);

		ProductDetailInfo productDetailInfo = ProductDetailInfo.from(product, brand, likeCount);

		redisTemplate.opsForValue().set(key, productDetailInfo, Duration.ofMinutes(10));

		return productDetailInfo;
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
