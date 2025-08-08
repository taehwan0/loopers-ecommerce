package com.loopers.application.coupon;

import com.loopers.domain.coupon.UserCouponEntity;
import java.math.BigDecimal;

public record UserCouponInfo(
		Long userId,
		Long couponId,
		String couponType,
		BigDecimal discountValue
) {
	public static UserCouponInfo from(UserCouponEntity userCoupon) {
		return new UserCouponInfo(
				userCoupon.getUserId(),
				userCoupon.getId(),
				userCoupon.getCouponPolicy().getType().name(),
				userCoupon.getCouponPolicy().getDiscountValue()
		);
	}
}
