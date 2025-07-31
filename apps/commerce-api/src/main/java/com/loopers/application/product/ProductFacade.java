package com.loopers.application.product;

import com.loopers.domain.brand.BrandService;
import com.loopers.domain.like.LikeCountEntity;
import com.loopers.domain.like.LikeService;
import com.loopers.domain.product.ProductService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductFacade {

	private final ProductService productService;
	private final BrandService brandService;
	private final LikeService likeService;

	@Transactional(readOnly = true)
	public ProductDetailInfo getProductDetail(Long productId) {
		var product = productService.getProduct(productId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[productId = " + productId + "] 상품을 찾을 수 없습니다."));

		var brand = brandService.getBrand(product.getBrandId())
				.orElseThrow(
						() -> new CoreException(ErrorType.NOT_FOUND, "[brandId = " + product.getBrandId() + "] 브랜드를 찾을 수 없습니다."));

		LikeCountEntity likeCount = likeService.getProductLikeCount(productId);

		return ProductDetailInfo.from(product, brand, likeCount);
	}
}
