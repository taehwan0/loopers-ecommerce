package com.loopers.interfaces.api.ranking;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.PageResponse;
import com.loopers.interfaces.api.PageResponse.PageMeta;
import com.loopers.interfaces.api.ranking.RankingV1Dto.ProductResponse;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/rankings")
@RestController
public class RankingV1Controller implements RankingV1Spec {

	@GetMapping("")
	@Override
	public ApiResponse<PageResponse<ProductResponse>> getRanking(
			@RequestParam("date") String date,
			@RequestParam("size") int size,
			@RequestParam("page") int page
	) {
		return ApiResponse.success(
				PageResponse.from(Collections.emptyList(),
						PageMeta.of(page, size, 0, 0))
		);
	}
}
