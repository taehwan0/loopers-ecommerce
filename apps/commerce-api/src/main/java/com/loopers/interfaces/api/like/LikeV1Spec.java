package com.loopers.interfaces.api.like;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;

public interface LikeV1Spec {

	@Operation(
			summary = "상품 좋아요",
			description = "상품 좋아요를 등록 합니다."
	)
	ApiResponse<Object> likeProduct(
			String loginId,
			Long productId
	);

	@Operation(
			summary = "상품 좋아요 취소",
			description = "상품 좋아요 등록을 취소합니다."
	)
	ApiResponse<Object> unlikeProduct(
			String loginId,
			Long productId
	);
}
