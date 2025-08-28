package com.loopers.domain.like;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.loopers.application.like.LikeFacade;
import com.loopers.application.like.LikeProductCommand;
import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.Stock;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.vo.Price;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.like.LikeCountJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

@RecordApplicationEvents
@SpringBootTest
class LikeIntegrationTest {

	private static final String REGISTERED_USER_ID = "ADMIN";
	private static final String REGISTERED_BRAND_NAME = "NIKE";
	private static final String REGISTERED_PRODUCT_ID = "SHOES-001";

	@Autowired
	LikeFacade likeFacade;

	@MockitoSpyBean
	LikeService likeService;

	@MockitoSpyBean
	LikeRepository likeRepository;

	@Autowired
	DatabaseCleanUp databaseCleanUp;

	@Autowired
	UserJpaRepository userJpaRepository;

	@Autowired
	ProductJpaRepository productJpaRepository;

	@Autowired
	BrandJpaRepository brandJpaRepository;

	@Autowired
	LikeCountJpaRepository likeCountJpaRepository;

	@Autowired
	ApplicationEvents applicationEvents;

	UserEntity createUser() {
		return userJpaRepository.save(UserEntity.of(
				REGISTERED_USER_ID,
				"ADMIN",
				Gender.M,
				"2025-01-01",
				"admin@example.com"
		));
	}

	UserEntity createUser(int count) {
		return userJpaRepository.save(UserEntity.of(
				REGISTERED_USER_ID + count,
				"ADMIN",
				Gender.M,
				"2025-01-01",
				"admin@example.com"
		));
	}

	LikeCountEntity createLikeCount(Long productId) {
		LikeCountEntity likeCount = LikeCountEntity.of(LikeTarget.of(productId, LikeTargetType.PRODUCT));
		return likeCountJpaRepository.save(likeCount);
	}

	ProductEntity createProduct() {
		BrandEntity brand = brandJpaRepository.save(BrandEntity.of(REGISTERED_BRAND_NAME, "description"));

		ProductEntity product = ProductEntity.of(
				REGISTERED_PRODUCT_ID,
				brand.getId(),
				Price.of(1000L),
				Stock.of(10),
				LocalDate.of(2025, 1, 1)
		);

		return productJpaRepository.save(product);
	}

	@AfterEach
	void tearDown() {
		this.databaseCleanUp.truncateAllTables();
	}

	@DisplayName("상품 Like 테스트")
	@Nested
	class ProductLikeTest {

		@DisplayName("상품 Like 등록 시 존재하지 않는 사용자의 LoginID를 입력한다면, Not Found 에러가 발생한다.")
		@Test
		void failLikeWithNotFound_whenUserIdIsNotExists() {
			// arrange
			final String loginId = "UNKNOWN";
			ProductEntity product = createProduct();
			LikeProductCommand likeProductCommand = LikeProductCommand.of(loginId, product.getId());

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> likeFacade.likeProduct(likeProductCommand)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND)
			);
		}

		@DisplayName("상품 Like 등록 시 존재하지 않는 상품의 ID를 입력한다면, Not Found 에러가 발생한다.")
		@Test
		void failLikeWithNotFound_whenProductIdIsNotExists() {
			// arrange
			UserEntity user = createUser();
			final Long productId = -999L;
			LikeProductCommand likeProductCommand = LikeProductCommand.of(user.getLoginId(), productId);

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> likeFacade.likeProduct(likeProductCommand)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND)
			);
		}

		@DisplayName("상품 Like 해제 시 존재하지 않는 사용자의 ID를 입력한다면, Not Found 에러가 발생한다.")
		@Test
		void failUnlikeWithNotFound_whenUserIdIsNotExists() {
			// arrange
			final String loginId = "UNKNOWN";
			ProductEntity product = createProduct();
			LikeProductCommand likeProductCommand = LikeProductCommand.of(loginId, product.getId());

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> likeFacade.unlikeProduct(likeProductCommand)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND)
			);
		}

		@DisplayName("상품 Like 해제 시 존재하지 않는 상품의 ID를 입력한다면, Not Found 에러가 발생한다.")
		@Test
		void failUnlikeWithNotFound_whenProductIdIsNotExists() {
			// arrange
			UserEntity user = createUser();
			final Long productId = -999L;
			LikeProductCommand likeProductCommand = LikeProductCommand.of(user.getLoginId(), productId);

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> likeFacade.unlikeProduct(likeProductCommand)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND)
			);
		}

		@DisplayName("상품 Like 등록이 두 번 요청되어도 정상적으로 수행되며, 하나의 Like만 등록된다. (멱등성을 가진다.)")
		@Test
		void registerLike_whenTwoRequestIsCalled() {
			// arrange
			UserEntity user = createUser();
			ProductEntity product = createProduct();
			LikeProductCommand likeProductCommand = LikeProductCommand.of(user.getLoginId(), product.getId());

			// act
			likeFacade.likeProduct(likeProductCommand);
			likeFacade.likeProduct(likeProductCommand);

			// assert
			assertAll(
					() -> verify(likeService, times(2)).likeProduct(user.getId(), product.getId()),
					() -> verify(likeRepository, times(1)).save(
							argThat(
									like -> like.getUserId().equals(user.getId())
											&& like.getLikeTarget().getTargetId().equals(product.getId())
											&& like.getLikeTarget().getTargetType().equals(LikeTargetType.PRODUCT)
							)
					)
			);
		}

		@DisplayName("Like 등록이 없는 상품을 Like 등록 해제 하더라도 정상적으로 동작한다. (멱등성을 가진다.)")
		@Test
		void successUnlike_whenLikeIsNotRegistered() {
			// arrange
			UserEntity user = createUser();
			ProductEntity product = createProduct();
			LikeProductCommand likeProductCommand = LikeProductCommand.of(user.getLoginId(), product.getId());

			// act
			likeFacade.unlikeProduct(likeProductCommand);

			// assert
			assertAll(
					() -> verify(likeService, times(1)).unlikeProduct(user.getId(), product.getId()),
					() -> verify(likeRepository, times(0)).delete(any())
			);
		}
	}

	@DisplayName("상품 Like 동시성 테스트")
	@Nested
	class ProductLikeConcurrency {

		@DisplayName("동시에 100명의 사용자가 좋아요를 시도했을 때 정상적으로 반영되어 집계된다.")
		@Test
		void success_whenMultipleUsersLike() throws InterruptedException {
			// arrange
			int numberOfThreads = 100;
			CountDownLatch countDownLatch = new CountDownLatch(numberOfThreads);
			ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

			ProductEntity product = createProduct();
			createLikeCount(product.getId());

			List<UserEntity> users = IntStream.range(0, numberOfThreads)
					.mapToObj(LikeIntegrationTest.this::createUser)
					.toList();

			// act
			for (int i = 0; i < numberOfThreads; i++) {
				final int idx = i;
				executor.submit(() -> {
							try {
								LikeProductCommand command = LikeProductCommand.of(users.get(idx).getLoginId(), product.getId());
								likeFacade.likeProduct(command);
							} catch (Exception e) {
								System.out.println("fail" + e);
							} finally {
								countDownLatch.countDown();
							}
						}
				);
			}

			countDownLatch.await();
			executor.shutdown();

			// assert
			long count = applicationEvents.stream(LikeEvent.ProductLike.class).count();
			assertThat(count).isEqualTo(100L);
		}
	}
}
