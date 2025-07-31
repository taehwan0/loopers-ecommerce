package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
@Entity
public class ProductEntity extends BaseEntity {
	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "brand_id", nullable = false, updatable = false)
	private Long brandId;

	@Embedded
	private Price price;

	@Embedded
	private Stock stock;

	@Column(name = "release_date", nullable = false)
	private LocalDate releaseDate;

	private ProductEntity(String name, Long brandId, Price price, Stock stock, LocalDate releaseDate) {
		if (name == null || name.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "상품의 이름은 비어있을 수 없습니다.");
		}

		if (name.trim().length() > 255) {
			throw new CoreException(ErrorType.BAD_REQUEST, "상품의 이름은 양끝 공백을 제외하고 255자를 초과할 수 없습니다.");
		}

		if (brandId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "상품은 브랜드에 속해야합니다.");
		}

		if (price == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "상품의 가격은 비어있을 수 없습니다.");
		}

		if (stock == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "상품의 재고는 비어있을 수 없습니다.");
		}

		if (releaseDate == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "상품의 발매일 비어있을 수 없습니다.");
		}

		this.name = name.trim();
		this.brandId = brandId;
		this.price = price;
		this.stock = stock;
		this.releaseDate = releaseDate;
	}

	public static ProductEntity of(String name, Long brandId, Price price, Stock stock, LocalDate releaseDate) {
		return new ProductEntity(name, brandId, price, stock, releaseDate);
	}
}
