package com.loopers.domain.coupon;


import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "coupon_policy")
public class CouponPolicyEntity extends BaseEntity {

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "coupon_type", nullable = false)
	private CouponType type;

	@Column(name = "discount_value", nullable = false)
	private BigDecimal discountValue;

	/*
	발급 가능 개수
	발급 가능 상태 여부
	등을 포함한 쿠폰 정책으로 확장 가능
	 */

	private CouponPolicyEntity(String name, CouponType type, BigDecimal discountValue) {
		if (name == null || name.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰 이름은 비어있을 수 없습니다.");
		}

		if (name.length() > 255) {
			throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰 이름은 255자를 초과할 수 없습니다.");
		}

		if (type == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "쿠폰 타입은 비어있을 수 없습니다.");
		}

		if (discountValue == null || discountValue.compareTo(BigDecimal.ZERO) <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "할인 금액은 0보다 커야 합니다.");
		}

		if (CouponType.FIXED_AMOUNT == type && discountValue.compareTo(BigDecimal.ZERO) <= 0) {
				throw new CoreException(ErrorType.BAD_REQUEST, "할인 금액은 0보다 커야 합니다.");
		}

		if (CouponType.FIXED_RATE == type && discountValue.compareTo(BigDecimal.valueOf(100)) > 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "할인 비율은 100을 초과할 수 없습니다.");
		}

		this.name = name;
		this.type = type;
		this.discountValue = discountValue;
	}

	public static CouponPolicyEntity of(String name, CouponType type, BigDecimal discountValue) {
		return new CouponPolicyEntity(name, type, discountValue);
	}
}
