package com.loopers.domain.order;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderEntityTest {

	@DisplayName("주문 생성 시 사용자의 ID가 비어있다면, Bad Request 에러가 발생해 실패한다.")
	@Test
	void failWithBadRequest_whenUserIdIsNull() {
		// arrange
		final UUID idempotencyKey = UUID.randomUUID();

		// act
		CoreException exception = assertThrows(
				CoreException.class,
				() -> OrderEntity.of(idempotencyKey, null)
		);

		// assert
		assertAll(
				() -> assertThat(exception).isNotNull(),
				() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
		);
	}

	@DisplayName("주문 생성 시 멱등키가 없다면 Bad Request 에러가 발생해 실패한다.")
	@Test
	void failWithBadRequest_whenIdempotencyKeyIsNull() {
		// arrange
		final Long userId = 1L;

		// act
		CoreException exception = assertThrows(
				CoreException.class,
				() -> OrderEntity.of(null, userId)
		);

		// assert
		assertAll(
				() -> assertThat(exception).isNotNull(),
				() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
		);
	}

	@DisplayName("주문 상태 변경 시 null 값이 입력되면, Bad Request 에러가 발생해 실패한다.")
	@Test
	void failWithBadRequest_whenOrderStatusIsNull() {
		// arrange
		final UUID idempotencyKey = UUID.randomUUID();
		final Long userId = 1L;
		final OrderEntity orderEntity = OrderEntity.of(idempotencyKey, userId);

		// act
		CoreException exception = assertThrows(
				CoreException.class,
				() -> orderEntity.updateStatus(null)
		);

		// assert
		assertAll(
				() -> assertThat(exception).isNotNull(),
				() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
		);
	}
}

