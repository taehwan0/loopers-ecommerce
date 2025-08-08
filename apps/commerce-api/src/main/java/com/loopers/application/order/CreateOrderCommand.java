package com.loopers.application.order;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.List;
import java.util.UUID;

public record CreateOrderCommand(
		UUID idempotencyKey,
		String loginId,
		Long couponId,
		List<CreateOrderItem> items
) {

	public CreateOrderCommand {
		if (idempotencyKey == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "idempotencyKey 필수 입력값입니다.");
		}

		if (loginId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "loginId 필수 입력값입니다.");
		}

		if (items == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문 항목은 필수 입력값 입니다.");
		}
	}

	public static CreateOrderCommand of(UUID idempotencyKey, String loginId, Long couponId, List<CreateOrderItem> items) {
		return new CreateOrderCommand(idempotencyKey, loginId, couponId, items);
	}

	public record CreateOrderItem(
			Long productId,
			int quantity
	) {

		public CreateOrderItem {
			if (productId == null) {
				throw new CoreException(ErrorType.BAD_REQUEST, "productId 필수 입력값입니다.");
			}

			if (quantity <= 0) {
				throw new CoreException(ErrorType.BAD_REQUEST, "quantity는 1이상이어야 합니다.");
			}
		}

		public static CreateOrderItem of(Long productId, int quantity) {
			return new CreateOrderItem(productId, quantity);
		}
	}
}
