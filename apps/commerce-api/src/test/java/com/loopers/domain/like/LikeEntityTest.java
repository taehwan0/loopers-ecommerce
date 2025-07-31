package com.loopers.domain.like;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class LikeEntityTest {

	@DisplayName("Like 생성 테스트")
	@Nested
	class CreateLikeEntity {

		@DisplayName("ProductLike 생성 시 사용자 id가 없다면, Bad Reqeust 에러가 발생한다.")
		@Test
		void failWithBadRequest_whenUserIdIsNull() {
			// arrange
			final Long productId = 1L;
			LikeTarget likeTarget = LikeTarget.of(productId, LikeTargetType.PRODUCT);

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> LikeEntity.createProductLike(null, likeTarget)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
			);
		}

		@DisplayName("ProductLike 생성 시 상품 id가 없다면, Bad Reqeust 에러가 발생한다.")
		@Test
		void failWithBadRequest_productIdIsNull() {
			// arrange
			final Long userId = 1L;

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> LikeEntity.createProductLike(userId, null)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
			);
		}

		@DisplayName("ProductLike 생성 시, 유효한 값이 입력되면, 정상적으로 생성된다.")
		@Test
		void createProductLike_whenValidValueIsProvided() {
			// arrange
			final Long userId = 1L;
			final Long productId = 2L;

			// act
			LikeEntity productLike = LikeEntity.createProductLike(userId, LikeTarget.of(productId, LikeTargetType.PRODUCT));

			// assert
			assertAll(
					() -> assertThat(productLike).isNotNull(),
					() -> assertThat(productLike.getUserId()).isEqualTo(userId),
					() -> assertThat(productLike.getLikeTarget().getTargetId()).isEqualTo(productId),
					() -> assertThat(productLike.getLikeTarget().getTargetType()).isEqualTo(LikeTargetType.PRODUCT)
			);
		}
	}
}
