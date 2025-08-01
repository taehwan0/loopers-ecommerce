package com.loopers.application.order;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public record PointPaymentCommand(
		Long userId,
		Long orderId
) {

	public PointPaymentCommand {
		if (userId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "userId 필수 입력값입니다.");
		}

		if (orderId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "orderId 필수 입력값입니다.");
		}
	}

	public static PointPaymentCommand of(Long userId, Long orderId) {
		return new PointPaymentCommand(userId, orderId);
	}
}
