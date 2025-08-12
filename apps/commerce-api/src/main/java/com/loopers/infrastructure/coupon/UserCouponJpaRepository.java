package com.loopers.infrastructure.coupon;

import com.loopers.domain.coupon.UserCouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponJpaRepository extends JpaRepository<UserCouponEntity, Long> {
	boolean existsByUserIdAndCouponPolicyId(Long userId, Long couponPolicyId);
}
