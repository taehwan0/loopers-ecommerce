package com.loopers.domain.order;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public record CreateOrderItemDTO(
		Long productId,
		int quantity
) {

	public CreateOrderItemDTO {
		if (productId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "productId 필수 입력값입니다.");
		}

		if (quantity <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "quantity는 1이상이어야 합니다.");
		}
	}

	public static CreateOrderItemDTO of(Long productId, int quantity) {
		return new CreateOrderItemDTO(productId, quantity);
	}
}
