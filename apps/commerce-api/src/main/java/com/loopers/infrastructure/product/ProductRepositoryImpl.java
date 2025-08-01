package com.loopers.infrastructure.product;

import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepository {

	private final ProductJpaRepository productJpaRepository;

	@Override
	public Optional<ProductEntity> findById(Long id) {
		return productJpaRepository.findById(id);
	}
}
