package com.loopers.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserEntity;
import com.loopers.infrastructure.coupon.CouponPolicyJpaRepository;
import com.loopers.infrastructure.coupon.UserCouponJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CouponIntegrationTest {

	@Autowired
	CouponService couponService;

	@Autowired
	UserJpaRepository userJpaRepository;

	@Autowired
	CouponPolicyJpaRepository  couponPolicyJpaRepository;

	@Autowired
	UserCouponJpaRepository userCouponJpaRepository;

	@Autowired
	DatabaseCleanUp databaseCleanUp;

	@AfterEach
	void cleanUp() {
		databaseCleanUp.truncateAllTables();
	}

	UserEntity createUser() {
		UserEntity user = UserEntity.of(
				"test",
				"테스트",
				Gender.M,
				"2023-01-01",
				"foo@example.com"
		);

		return userJpaRepository.save(user);
	}

	CouponPolicyEntity createCouponPolicy(String name, CouponType type, BigDecimal discountAmount) {
		CouponPolicyEntity couponPolicy = CouponPolicyEntity.of(name, type, discountAmount);
		return couponPolicyJpaRepository.save(couponPolicy);
	}

	@DisplayName("쿠폰 발급 테스트")
	@Nested
	class IssueCoupon {

		@DisplayName("쿠폰을 이미 발급했다면, CoreException(CONFLICT) 에러가 발생한다.")
		@Test
		void issueCouponAlreadyIssued() {
			// arrange
			UserEntity user = createUser();
			CouponPolicyEntity couponPolicy = createCouponPolicy("쿠폰", CouponType.FIXED_AMOUNT, BigDecimal.valueOf(1000));

			// act
			couponService.issueCoupon(user.getId(), couponPolicy); // 첫 번째 발급은 성공

			CoreException exception = assertThrows(
					CoreException.class,
					() -> couponService.issueCoupon(user.getId(), couponPolicy)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.CONFLICT)
			);
		}
	}
}
