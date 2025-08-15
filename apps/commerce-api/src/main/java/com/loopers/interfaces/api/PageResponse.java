package com.loopers.interfaces.api;

import java.util.List;

public record PageResponse<T>(
		List<T> list,
		PageMeta page
) {

	public static <T> PageResponse<T> from(List<T> list, PageMeta pageMeta) {
		return new PageResponse<>(
				list,
				pageMeta
		);
	}


	public record PageMeta(
			int currentPage,
			int size,
			long totalElements,
			int totalPages
	) {

		public static PageMeta of(int currentPage, int size, long totalElements, int totalPages) {
			return new PageMeta(
					currentPage, size, totalElements, totalPages);
		}
	}
}
