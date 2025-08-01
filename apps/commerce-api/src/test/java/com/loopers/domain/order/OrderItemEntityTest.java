package com.loopers.domain.order;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class OrderItemEntityTest {

	@DisplayName("OrderItem 생성 테스트")
	@Nested
	class CreateOrderItem {

		@DisplayName("주문이 null이라면, Bad Request 에러가 발생해 실패한다.")
		@Test
		void failWithBadRequest_whenOrderIdIsNull() {
			// arrange
			final Long productId = 1L;
			final int quantity = 2;

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> OrderItemEntity.of(null, productId, quantity)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
			);
		}

		@DisplayName("상품 ID가 비어있다면, Bad Request 에러가 발생해 실패한다.")
		@Test
		void failWithBadRequest_whenProductIdIsNull() {
			// arrange
			OrderEntity order = OrderEntity.of(UUID.randomUUID(), 1L);
			final int quantity = 2;

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> OrderItemEntity.of(order, null, quantity)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
			);
		}

		@DisplayName("수량이 0 이하라면, Bad Request 에러가 발생해 실패한다.")
		@Test
		void failWithBadRequest_whenQuantityIsZeroOrLess() {
			// arrange
			OrderEntity order = OrderEntity.of(UUID.randomUUID(), 1L);
			final Long productId = 1L;
			final int quantity = 0;

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> OrderItemEntity.of(order, productId, quantity)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
			);
		}
	}
}
