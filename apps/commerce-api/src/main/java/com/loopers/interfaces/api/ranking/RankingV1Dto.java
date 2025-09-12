package com.loopers.interfaces.api.ranking;

public class RankingV1Dto {

	public record ProductResponse(
			Long id,
			String name,
			long price,
			Long brandId,
			String brandName,
			int likeCount
	) {

	}
}
