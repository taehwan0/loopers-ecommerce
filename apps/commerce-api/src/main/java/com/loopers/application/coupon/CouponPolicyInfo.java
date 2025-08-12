package com.loopers.application.coupon;

import com.loopers.domain.coupon.CouponPolicyEntity;
import java.math.BigDecimal;

public record CouponPolicyInfo(
		Long id,
		String name,
		String couponType,
		BigDecimal discountValue

) {

	public static CouponPolicyInfo from(CouponPolicyEntity entity) {
		return new CouponPolicyInfo(
				entity.getId(),
				entity.getName(),
				entity.getType().name(),
				entity.getDiscountValue()
		);
	}
}
