package com.loopers.application.like;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public record LikeProductCommand(
		Long userId,
		Long productId
) {

	public LikeProductCommand {
		if (userId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "userId 필수 입력값입니다.");
		}

		if (productId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "productId는 필수 입력값입니다.");
		}
	}

	public static LikeProductCommand of(Long userId, Long productId) {
		return new LikeProductCommand(userId, productId);
	}
}
