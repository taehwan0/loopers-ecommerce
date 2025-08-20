package com.loopers.domain.product;


import java.util.Optional;

public interface ProductQueryCacheRepository {

	Optional<ProductSummary> getProductSummary(Long productId);

	void saveProductSummary(ProductSummary productSummary);
}
