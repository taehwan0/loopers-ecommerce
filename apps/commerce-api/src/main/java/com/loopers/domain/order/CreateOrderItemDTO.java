package com.loopers.domain.order;

import com.loopers.domain.vo.Price;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public record CreateOrderItemDTO(
		Long productId,
		Price price,
		int quantity
) {

	public CreateOrderItemDTO {
		if (productId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "productId 필수 입력값입니다.");
		}

		if (price == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "price 필수 입력값입니다.");
		}

		if (quantity <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "quantity는 1이상이어야 합니다.");
		}

		price = Price.of(price.getAmount());
	}

	public static CreateOrderItemDTO of(Long productId, Price price, int quantity) {
		return new CreateOrderItemDTO(productId, price, quantity);
	}
}
