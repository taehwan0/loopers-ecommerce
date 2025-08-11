package com.loopers.domain.order;

import com.loopers.domain.vo.Price;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public record OrderItemCriteria(
		Long productId,
		Price price,
		int quantity
) {

	public OrderItemCriteria {
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

	public static OrderItemCriteria of(Long productId, Price price, int quantity) {
		return new OrderItemCriteria(productId, price, quantity);
	}
}
