package com.loopers.domain.coupon;


import java.util.Optional;

public interface CouponRepository {

	CouponPolicyEntity save(CouponPolicyEntity couponPolicyEntity);

	Optional<CouponPolicyEntity> findById(Long id);

	UserCouponEntity save(UserCouponEntity userCouponEntity);

	boolean isAlreadyIssued(Long userId, Long couponPolicyId);
}
