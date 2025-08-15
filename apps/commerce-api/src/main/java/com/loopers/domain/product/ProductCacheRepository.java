package com.loopers.domain.product;

import java.util.Optional;

public interface ProductCacheRepository {

	Optional<ProductEntity> findById(Long id);

	void save(ProductEntity productEntity);
}
