package com.loopers.domain.coupon;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_coupon")
public class UserCouponEntity extends BaseEntity {

	@Column(name = "user_id", nullable = false, updatable = false)
	private Long userId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "coupon_policy_id", nullable = false, updatable = false)
	private CouponPolicyEntity couponPolicy;

	@Column(name = "is_used", nullable = false)
	private boolean isUsed;

	private UserCouponEntity(Long userId, CouponPolicyEntity couponPolicy) {
		if (userId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "userId는 비어있을 수 없습니다.");
		}

		if (couponPolicy == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "couponPolicy는 비어있을 수 없습니다.");
		}

		this.userId = userId;
		this.couponPolicy = couponPolicy;
		this.isUsed = false;
	}

	public static UserCouponEntity of(Long userId, CouponPolicyEntity couponPolicy) {
		return new UserCouponEntity(userId, couponPolicy);
	}

	public void use() {
		this.isUsed = true;
	}
}
