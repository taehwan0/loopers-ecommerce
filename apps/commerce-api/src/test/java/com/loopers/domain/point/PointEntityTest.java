package com.loopers.domain.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PointEntityTest {

	@DisplayName("포인트를 충전할 때,")
	@Nested
	class ChargePoint {

		@DisplayName("충전이 성공하면, 현재 포인트를 반환한다.")
		@Test
		void chargePoint_whenChargePointSuccess() {
			// arrange
			PointAccountEntity pointAccount = PointAccountEntity.of(1L);
			Point currentBalance = pointAccount.getPointBalance();
			Point chargePoint = Point.of(100L);

			// act
			pointAccount.charge(chargePoint);

			// assert
			assertThat(pointAccount.getPointBalance()).isEqualTo(currentBalance.add(chargePoint));
		}

		@DisplayName("0이하의 포인트를 충전하려고 하면, 400 Bad Request 에러가 발생한다.")
		@ParameterizedTest
		@ValueSource(longs = {Long.MIN_VALUE, -1L, 0L})
		void failWithBadRequest_whenPointValueIsZeroOrNegative(long value) {
			// arrange
			PointAccountEntity pointAccount = PointAccountEntity.of(1L);
			Point chargePoint = Point.of(value);

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> pointAccount.charge(chargePoint)
			);

			// assert
			assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
		}
	}
}
