package com.loopers.domain.product;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductService {

	private final ProductRepository productRepository;

	public Optional<ProductEntity> getProduct(Long id) {
		return productRepository.findById(id);
	}
}
