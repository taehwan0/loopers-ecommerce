package com.loopers.domain.coupon;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CouponService {

	private final CouponRepository couponRepository;

	public CouponPolicyEntity createCoupon(String name, CouponType type,  BigDecimal discountAmount) {
		CouponPolicyEntity couponPolicy = CouponPolicyEntity.of(name, type, discountAmount);
		return couponRepository.save(couponPolicy);
	}

	public CouponPolicyEntity getCouponPolicy(Long couponPolicyId) {
		return couponRepository.findById(couponPolicyId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "쿠폰 정책을 찾을 수 없습니다. [couponPolicyId = " + couponPolicyId + "]"));
	}

	public UserCouponEntity issueCoupon(Long userId, CouponPolicyEntity couponPolicy) {
		if (couponRepository.isAlreadyIssued(userId, couponPolicy.getId())) {
			throw new CoreException(ErrorType.CONFLICT, "이미 해당 쿠폰을 발급했습니다. [userId = " + userId + ", couponPolicyId = " + couponPolicy.getId() + "]");
		}

		UserCouponEntity userCoupon = UserCouponEntity.of(userId, couponPolicy);

		return couponRepository.save(userCoupon);
	}
}
