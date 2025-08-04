package com.loopers.domain.product;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.support.error.payment.PaymentException;
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

	public void decreaseStock(Long productId, int quantity) {
		ProductEntity product = productRepository.findById(productId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다."));

		if (product.getStock().getQuantity() < quantity) {
			throw new PaymentException(ErrorType.CONFLICT, "재고가 부족합니다. 현재 재고: " + product.getStock().getQuantity() + ", 요청 수량: " + quantity);
		}

		product.decreaseStock(quantity);
	}
}
