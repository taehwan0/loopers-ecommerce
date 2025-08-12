package com.loopers.application.coupon;

import com.loopers.domain.coupon.CouponPolicyEntity;
import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.coupon.CouponType;
import com.loopers.domain.coupon.UserCouponEntity;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CouponFacade {

	private final CouponService couponService;
	private final UserService userService;

	@Transactional
	public CouponPolicyInfo createCouponPolicy(CreateCouponPolicyCommand command) {
		CouponPolicyEntity coupon = couponService.createCoupon(
				command.name(),
				CouponType.valueOf(command.couponType()),
				command.discountValue()
		);

		return CouponPolicyInfo.from(coupon);
	}

	@Transactional
	public UserCouponInfo issueCoupon(IssueCouponCommand command) {
		UserEntity user = userService.findByLoginId(command.loginId())
				.orElseThrow(
						() -> new CoreException(ErrorType.NOT_FOUND, "[loginId = " + command.loginId() + "] 사용자를 찾을 수 없습니다."));

		CouponPolicyEntity couponPolicy = couponService.getCouponPolicy(command.couponPolicyId());

		UserCouponEntity userCouponEntity = couponService.issueCoupon(user.getId(), couponPolicy);

		return UserCouponInfo.from(userCouponEntity);
	}
}
