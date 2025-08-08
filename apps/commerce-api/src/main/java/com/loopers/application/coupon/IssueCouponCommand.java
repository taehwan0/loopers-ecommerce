package com.loopers.application.coupon;

public record IssueCouponCommand(
		String loginId,
		Long couponPolicyId
) {

}
