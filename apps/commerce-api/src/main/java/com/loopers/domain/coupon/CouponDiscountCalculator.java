package com.loopers.domain.coupon;

import com.loopers.domain.vo.Price;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;

@Service
public class CouponDiscountCalculator {

	public Price calculateDiscount(Price price, CouponPolicyEntity couponPolicy) {
		CouponType type = couponPolicy.getType();

		switch (type) {
			case CouponType.FIXED_AMOUNT -> {
				BigDecimal discountedPrice = BigDecimal.valueOf(price.getAmount()).subtract(couponPolicy.getDiscountValue());
				if (discountedPrice.compareTo(BigDecimal.ZERO) < 0) {
					discountedPrice = BigDecimal.ZERO;
				}

				return Price.of(discountedPrice.longValue());
			}
			case CouponType.FIXED_RATE -> {
				BigDecimal discountRate = couponPolicy.getDiscountValue().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
				BigDecimal discountedPrice = BigDecimal.valueOf(price.getAmount()).multiply(BigDecimal.ONE.subtract(discountRate));

				return Price.of(discountedPrice.longValue());
			}
			default -> throw new CoreException(ErrorType.BAD_REQUEST, "지원하지 않는 쿠폰 타입 입니다.");
		}
	}
}
