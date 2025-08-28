package com.loopers.domain.product;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductService {

	private final ProductRepository productRepository;

	public ProductEntity getProduct(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND,
						"[productId = " + id + "] 상품을 찾을 수 없습니다."));
	}

	public void decreaseStock(Long productId, int quantity) {
		ProductEntity product = productRepository.findById(productId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));

		if (product.getStock().getQuantity() < quantity) {
			throw new CoreException(ErrorType.CONFLICT, "상품의 재고가 부족합니다.");
		}

		product.decreaseStock(quantity);
	}

	public void increaseStock(Long productId, int quantity) {
		ProductEntity product = productRepository.findById(productId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));

		product.increaseStock(quantity);
	}
}
