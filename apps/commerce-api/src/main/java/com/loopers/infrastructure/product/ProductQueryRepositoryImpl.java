package com.loopers.infrastructure.product;

import com.loopers.domain.product.Price;
import com.loopers.domain.product.ProductQueryRepository;
import com.loopers.domain.product.ProductSummary;
import com.loopers.domain.product.ProductSummary.BrandSummary;
import com.loopers.domain.product.ProductSummarySort;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductQueryRepositoryImpl implements ProductQueryRepository {

	private final EntityManager entityManager;

	@Override
	public Page<ProductSummary> getProductSummaries(ProductSummarySort sortBy, int page, int size) {
		String jpql = """
				SELECT
					p.id,
					p.name,
					p.price.amount,
					p.releaseDate,
					b.id,
					b.name,
					lc.likeCount
				FROM ProductEntity  p
					INNER JOIN BrandEntity b ON p.brandId = b.id
					LEFT OUTER JOIN LikeCountEntity lc ON lc.target.targetId = p.id AND lc.target.targetType = 'PRODUCT'
				ORDER BY
					CASE WHEN :sortBy = 'LATEST' THEN p.releaseDate END DESC,
					CASE WHEN :sortBy = 'PRICE_ASC' THEN p.price.amount END ASC,
					CASE WHEN :sortBy = 'LIKE_DESC' THEN lc.likeCount END DESC
				""";

		List<Object[]> results = entityManager.createQuery(jpql, Object[].class)
				.setParameter("sortBy", sortBy.name())
				.setFirstResult(page * size)
				.setMaxResults(size)
				.getResultList();

		List<ProductSummary> productSummaries = results.stream()
				.map(this::mapToProductSummary)
				.toList();

		long totalCount = getTotalCount();

		return new PageImpl<>(
				productSummaries,
				Pageable.ofSize(size).withPage(page),
				totalCount
		);
	}

	ProductSummary mapToProductSummary(Object[] r) {
		return new ProductSummary(
				(Long) r[0],
				(String) r[1],
				Price.of((Long) r[2]),
				(LocalDate) r[3],
				new BrandSummary((Long) r[4], (String) r[5]),
				r[6] != null ? ((Number) r[6]).intValue() : 0
		);
	}

	long getTotalCount() {
		String countJpql = "SELECT COUNT(p) FROM ProductEntity p";
		return entityManager.createQuery(countJpql, Long.class).getSingleResult();
	}
}
