package com.loopers.application.like;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public record LikeProductCommand(
		String loginId,
		Long productId
) {

	public LikeProductCommand {
		if (loginId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "loginId는 필수 입력값입니다.");
		}

		if (productId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "productId는 필수 입력값입니다.");
		}
	}

	public static LikeProductCommand of(String loginId, Long productId) {
		return new LikeProductCommand(loginId, productId);
	}
}
