package com.loopers.infrastructure.coupon;

import com.loopers.domain.coupon.CouponPolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponPolicyJpaRepository extends JpaRepository<CouponPolicyEntity, Long> {

}
