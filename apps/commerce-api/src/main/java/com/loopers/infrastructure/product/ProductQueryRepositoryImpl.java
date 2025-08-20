package com.loopers.infrastructure.product;

import com.loopers.domain.brand.QBrandEntity;
import com.loopers.domain.like.LikeTargetType;
import com.loopers.domain.like.QLikeCountEntity;
import com.loopers.domain.product.ProductQueryRepository;
import com.loopers.domain.product.ProductSummary;
import com.loopers.domain.product.ProductSummarySort;
import com.loopers.domain.product.QProductEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductQueryRepositoryImpl implements ProductQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Optional<ProductSummary> getProductSummary(Long productId) {
		var product = QProductEntity.productEntity;
		var brand = QBrandEntity.brandEntity;
		var likeCount = QLikeCountEntity.likeCountEntity;

		ProductSummary productSummary = jpaQueryFactory
				.select(
						Projections.constructor(
								ProductSummary.class,
								product.id,
								product.name,
								product.price,
								product.releaseDate,
								brand.id,
								brand.name,
								likeCount.likeCount
						)
				)
				.from(product)
				.join(brand).on(brand.id.eq(product.brandId))
				.leftJoin(likeCount).on(likeCount.target.targetId.eq(product.id))
				.where(product.id.eq(productId))
				.fetchFirst();

		return Optional.ofNullable(productSummary);
	}

	@Override
	public List<ProductSummary> getProductSummariesWithBrandFilter(Long brandId, ProductSummarySort sortBy, int page, int size) {
		var product = QProductEntity.productEntity;
		var brand = QBrandEntity.brandEntity;
		var likeCount = QLikeCountEntity.likeCountEntity;

		var sort = switch (sortBy) {
			case LATEST -> product.releaseDate.desc();
			case PRICE_ASC -> product.price.amount.asc();
			case LIKES_DESC -> likeCount.likeCount.desc();
		};

		return jpaQueryFactory
				.select(
						Projections.constructor(
								ProductSummary.class,
								product.id,
								product.name,
								product.price,
								product.releaseDate,
								brand.id,
								brand.name,
								likeCount.likeCount
						)
				)
				.from(likeCount)
				.join(product).on(product.id.eq(likeCount.target.targetId))
				.join(brand).on(brand.id.eq(product.brandId))
				.where(
						brandIdEq(brandId),
						likeCount.target.targetType.eq(LikeTargetType.PRODUCT)
				)
				.orderBy(sort)
				.offset((long) page * size)
				.limit(size)
				.fetch();
	}

	private BooleanExpression brandIdEq(Long brandId) {
		return brandId != null ? QProductEntity.productEntity.brandId.eq(brandId) : null;
	}

	@Override
	public long getTotalCount() {
		var product = QProductEntity.productEntity;

		Long count = jpaQueryFactory.select(product.id.count())
				.from(product)
				.fetchFirst();

		return count != null ? count : 0L;
	}

	@Override
	public long getTotalCountWithBrandFilter(Long brandId) {
		var product = QProductEntity.productEntity;

		Long count = jpaQueryFactory.select(product.id.count())
				.from(product)
				.where(product.brandId.eq(brandId))
				.fetchFirst();

		return count != null ? count : 0L;
	}
}
