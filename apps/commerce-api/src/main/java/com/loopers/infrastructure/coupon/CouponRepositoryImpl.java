package com.loopers.infrastructure.coupon;

import com.loopers.domain.coupon.CouponPolicyEntity;
import com.loopers.domain.coupon.CouponRepository;
import com.loopers.domain.coupon.UserCouponEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CouponRepositoryImpl implements CouponRepository {

	private final CouponPolicyJpaRepository couponPolicyJpaRepository;
	private final UserCouponJpaRepository userCouponJpaRepository;

	@Override
	public CouponPolicyEntity save(CouponPolicyEntity couponPolicy) {
		return couponPolicyJpaRepository.save(couponPolicy);
	}

	@Override
	public Optional<CouponPolicyEntity> findCouponPolicyById(Long id) {
		return couponPolicyJpaRepository.findById(id);
	}

	@Override
	public UserCouponEntity save(UserCouponEntity userCoupon) {
		return userCouponJpaRepository.save(userCoupon);
	}

	@Override
	public boolean isAlreadyIssued(Long userId, Long couponPolicyId) {
		return userCouponJpaRepository.existsByUserIdAndCouponPolicyId(userId, couponPolicyId);
	}

	@Override
	public Optional<UserCouponEntity> findUserCouponById(Long couponId) {
		return userCouponJpaRepository.findById(couponId);
	}
}
