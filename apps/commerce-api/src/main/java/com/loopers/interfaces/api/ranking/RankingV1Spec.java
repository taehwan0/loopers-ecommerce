package com.loopers.interfaces.api.ranking;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.PageResponse;
import com.loopers.interfaces.api.ranking.RankingV1Dto.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;

public interface RankingV1Spec {

	@Operation(
			summary = "상품 랭킹",
			description = "상품의 목록을 일일 랭킹으로 반환합니다."
	)
	ApiResponse<PageResponse<ProductResponse>> getRanking(String date, int size, int page);
}
