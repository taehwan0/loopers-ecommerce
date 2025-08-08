package com.loopers.application.coupon;

import java.math.BigDecimal;

public record CreateCouponPolicyCommand(
		String name,
		String couponType,
		BigDecimal discountValue
) {
	public static CreateCouponPolicyCommand of(
			String name,
			String couponType,
			BigDecimal discountValue
	) {
		return new CreateCouponPolicyCommand(name, couponType, discountValue);
	}
}
