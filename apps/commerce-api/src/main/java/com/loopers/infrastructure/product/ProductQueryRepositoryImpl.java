package com.loopers.infrastructure.product;

import com.loopers.domain.product.ProductQueryRepository;
import com.loopers.domain.product.ProductSummary;
import com.loopers.domain.product.ProductSummary.BrandSummary;
import com.loopers.domain.product.ProductSummarySort;
import com.loopers.domain.vo.Price;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductQueryRepositoryImpl implements ProductQueryRepository {

	private final EntityManager em;

	@Override
	public Page<ProductSummary> getProductSummaries(Long brandId, ProductSummarySort sortBy, int page, int size) {

		// 1) FROM / JOIN (공통)
		String select =
				"""
						SELECT
							p.id,
							p.name,
							p.price.amount,
							p.releaseDate,
							b.id,
							b.name,
							lc.likeCount
						""";

		String fromJoin =
				"""
						FROM ProductEntity p
						  INNER JOIN BrandEntity b ON p.brandId = b.id
						  LEFT JOIN LikeCountEntity lc
								 ON lc.target.targetId = p.id
								AND lc.target.targetType = 'PRODUCT'
						""";

		// 2) WHERE (brandId 있을 때만 붙이기)
		List<String> whereParts = new ArrayList<>();
		if (brandId != null) {
			whereParts.add("p.brandId = :brandId");
		}
		String where = whereParts.isEmpty() ? "" : "WHERE " + String.join(" AND ", whereParts) + "\n";

		// 3) ORDER BY (정렬별 고정)
		String orderBy;
		// COALESCE로 NULL 정렬 안정화 (LEFT JOIN이므로)
		switch (sortBy) {
			case LATEST -> orderBy = "ORDER BY p.releaseDate DESC\n";
			case PRICE_ASC -> orderBy = "ORDER BY p.price.amount ASC\n";
			case LIKES_DESC -> orderBy = "ORDER BY COALESCE(lc.likeCount, 0) DESC\n";
			default -> throw new IllegalArgumentException("Unknown sort: " + sortBy);
		}

		// 4) 본문 쿼리
		String jpql = select + fromJoin + where + orderBy;

		TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
		if (brandId != null) {
			query.setParameter("brandId", brandId);
		}
		query.setFirstResult(page * size);
		query.setMaxResults(size);

		List<Object[]> rows = query.getResultList();
		List<ProductSummary> content = rows.stream().map(this::mapToProductSummary).toList();

		// 5) 카운트도 동일 WHERE로
		long total = getTotalCount(brandId);

		return new PageImpl<>(
				content,
				Pageable.ofSize(size).withPage(page),
				total
		);
	}

	private ProductSummary mapToProductSummary(Object[] r) {
		return new ProductSummary(
				(Long) r[0],
				(String) r[1],
				Price.of((Long) r[2]),
				(LocalDate) r[3],
				new BrandSummary((Long) r[4], (String) r[5]),
				r[6] != null ? ((Number) r[6]).intValue() : 0
		);
	}

	private long getTotalCount(Long brandId) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(p) FROM ProductEntity p ");
		if (brandId != null) {
			sb.append("WHERE p.brandId = :brandId");
		}
		var countQuery = em.createQuery(sb.toString(), Long.class);
		if (brandId != null) {
			countQuery.setParameter("brandId", brandId);
		}
		return countQuery.getSingleResult();
	}
}
