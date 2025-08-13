package com.loopers.application.product;

import com.loopers.domain.product.ProductSummarySort;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public record ProductSummariesCommand(
		Long brandId,
		SortBy sortBy,
		int page,
		int size
) {

	public static ProductSummariesCommand of(
			Long brandId,
			SortBy sortBy,
			int page,
			int size
	) {
		return new ProductSummariesCommand(brandId, sortBy, page, size);
	}

	public ProductSummariesCommand {
		if (page < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "Page는 0 이상이어야 합니다.");
		}
		if (size <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "Size는 1 이상이어야 합니다.");
		}
	}

	public enum SortBy {
		LATEST,
		PRICE_ASC,
		LIKES_DESC,
		;

		public ProductSummarySort toProductSummarySort() {
			return switch (this) {
				case LATEST -> ProductSummarySort.LATEST;
				case PRICE_ASC -> ProductSummarySort.PRICE_ASC;
				case LIKES_DESC -> ProductSummarySort.LIKES_DESC;
			};
		}
	}
}
