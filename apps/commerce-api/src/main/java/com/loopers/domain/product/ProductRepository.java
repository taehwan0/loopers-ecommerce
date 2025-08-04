package com.loopers.domain.product;

import java.util.Optional;

public interface ProductRepository {

	Optional<ProductEntity> findById(Long id);
}
