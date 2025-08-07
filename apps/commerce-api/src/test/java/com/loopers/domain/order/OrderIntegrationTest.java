package com.loopers.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.loopers.application.order.CreateOrderCommand;
import com.loopers.application.order.CreateOrderCommand.CreateOrderItem;
import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.OrderInfo;
import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointAccountEntity;
import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.Stock;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.vo.Price;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.order.OrderJpaRepository;
import com.loopers.infrastructure.point.PointAccountJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class OrderIntegrationTest {

	@Autowired
	OrderFacade orderFacade;

	@MockitoSpyBean
	OrderService orderService;

	@Autowired
	BrandJpaRepository brandJpaRepository;

	@Autowired
	UserJpaRepository userJpaRepository;

	@Autowired
	ProductJpaRepository productJpaRepository;

	@Autowired
	OrderJpaRepository orderJpaRepository;

	@Autowired
	PointAccountJpaRepository pointAccountJpaRepository;

	@Autowired
	DatabaseCleanUp databaseCleanUp;

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllTables();
	}

	UserEntity createUser() {
		UserEntity user = UserEntity.of(
				"testUser",
				"테스트 유저",
				Gender.F,
				"1990-01-01",
				"foo@example.com");

		return userJpaRepository.save(user);
	}

	PointAccountEntity createPointAccount(UserEntity user, Point point) {
		PointAccountEntity pointAccount = PointAccountEntity.of(user.getId());
		pointAccount.charge(point);
		return pointAccountJpaRepository.save(pointAccount);
	}

	ProductEntity createProduct() {
		BrandEntity brand = brandJpaRepository.save(BrandEntity.of("NIKE", "나이키 브랜드"));

		return productJpaRepository.save(
				ProductEntity.of(
						"SHOES-001",
						brand.getId(),
						Price.of(1000L),
						Stock.of(10),
						LocalDate.of(2025, 1, 1)
				)
		);
	}

	@DisplayName("주문 생성 테스트")
	@Nested
	class CreateOrder {

		@DisplayName("주문 생성이 성공하면, PEDING 상태의 주문이 생성된다.")
		@Test
		void returnWithPendingStatus_whenOrderIsCreated() {
			// arrange
			UUID idempotencyKey = UUID.randomUUID();
			UserEntity user = createUser();
			ProductEntity product = createProduct();
			int quantity = 1;

			CreateOrderCommand command = CreateOrderCommand.of(
					idempotencyKey,
					user.getLoginId(),
					List.of(
							CreateOrderItem.of(product.getId(), quantity)
					)
			);

			// act
			OrderInfo order = orderFacade.createOrder(command);

			// assert
			assertAll(
					() -> assertThat(order).isNotNull(),
					() -> assertThat(order.orderStatus()).isEqualTo(OrderStatus.PENDING.name()),
					() -> assertThat(order.orderItems().getFirst().productId()).isEqualTo(product.getId()),
					() -> assertThat(order.orderItems().getFirst().quantity()).isEqualTo(quantity)
			);
		}

		@DisplayName("상품 생성 정보가 비어있다면, Bad Request 에러가 발생해 실패한다.")
		@Test
		void failWithBadRequest_whenOrderItemsIsEmpty() {
			// arrange
			final UUID idempotencyKey = UUID.randomUUID();
			UserEntity user = createUser();

			CreateOrderCommand command = CreateOrderCommand.of(
					idempotencyKey,
					user.getLoginId(),
					List.of()
			);

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> orderFacade.createOrder(command)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST)
			);
		}

		@DisplayName("존재하지 않는 사용자가 주문을 시도하면, NOT_FOUND 에러가 발생해 실패한다.")
		@Test
		void failWithNotFound_whenUserDoesNotExist() {
			// arrange
			final UUID idempotencyKey = UUID.randomUUID();
			final String nonExistentUserId = "UNKNOWN"; // 존재하지 않는 사용자 ID
			ProductEntity product = createProduct();
			final int quantity = 1;

			CreateOrderCommand command = CreateOrderCommand.of(
					idempotencyKey,
					nonExistentUserId,
					List.of(
							CreateOrderItem.of(product.getId(), quantity)
					)
			);

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> orderFacade.createOrder(command)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND)
			);
		}

		@DisplayName("존재하지 않는 상품으로 주문을 시도하면, NOT_FOUND 에러가 발생해 실패한다.")
		@Test
		void failWithNotFound_whenProductDoesNotExist() {
			// arrange
			final UUID idempotencyKey = UUID.randomUUID();
			UserEntity user = createUser();
			final Long nonExistentProductId = -1L; // 존재하지 않는 상품 ID
			final int quantity = 1;

			CreateOrderCommand command = CreateOrderCommand.of(
					idempotencyKey,
					user.getLoginId(),
					List.of(
							CreateOrderItem.of(nonExistentProductId, quantity)
					)
			);

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> orderFacade.createOrder(command)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND)
			);
		}

		@DisplayName("동일한 키로 주문 생성이 요청되면, 기존 요청이 반환된다. (멱등성 테스트)")
		@Test
		void returnExistingOrder_whenIdempotencyKeyIsSame() {
			// arrange
			UUID idempotencyKey = UUID.randomUUID();
			UserEntity user = createUser();
			ProductEntity product = createProduct();
			int quantity = 1;

			CreateOrderCommand command = CreateOrderCommand.of(
					idempotencyKey,
					user.getLoginId(),
					List.of(
							CreateOrderItem.of(product.getId(), quantity)
					)
			);

			// act
			OrderInfo firstOrder = orderFacade.createOrder(command);
			OrderInfo secondOrder = orderFacade.createOrder(command);

			// assert
			assertAll(
					() -> assertThat(firstOrder).isNotNull(),
					() -> assertThat(secondOrder).isNotNull(),
					() -> assertThat(firstOrder.id()).isEqualTo(secondOrder.id()),
					() -> assertThat(firstOrder.orderStatus()).isEqualTo(secondOrder.orderStatus()),
					() -> verify(orderService, times(1)).createOrder(any(UUID.class), anyLong(), anyList())
			);
		}
	}

	@DisplayName("주문 결제 테스트")
	@Nested
	class OrderPayment {

		@DisplayName("주문 포인트 결제를 성공하면, 결제 완료 상태로 변경된다.")
		@Test
		void returnWithPaidStatus_whenOrderIsPaidByPoint() {
			// arrange
			UserEntity user = createUser();
			ProductEntity product = createProduct();
			createPointAccount(user, Point.of(10000L));
			int quantity = 1;

			CreateOrderCommand command = CreateOrderCommand.of(
					UUID.randomUUID(),
					user.getLoginId(),
					List.of(
							CreateOrderItem.of(product.getId(), quantity)
					)
			);

			OrderInfo order = orderFacade.createOrder(command);

			// act
			OrderInfo orderInfo = orderFacade.payOrderByPoint(user.getLoginId(), order.id());

			// assert
			assertAll(
					() -> assertThat(orderInfo).isNotNull(),
					() -> assertThat(orderInfo.orderStatus()).isEqualTo(OrderStatus.PAYMENT_CONFIRMED.name())
			);
		}

		@Transactional
		@DisplayName("포인트가 부족하면, Conflict 에러가 발생해 실패한다.")
		@Test
		void failWithBadRequest_whenUserHasInsufficientPoints() {
			// arrange
			UserEntity user = createUser();
			ProductEntity product = createProduct();
			int quantity = 1;

			CreateOrderCommand command = CreateOrderCommand.of(
					UUID.randomUUID(),
					user.getLoginId(),
					List.of(
							CreateOrderItem.of(product.getId(), quantity)
					)
			);

			OrderInfo order = orderFacade.createOrder(command);

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> orderFacade.payOrderByPoint(user.getLoginId(), order.id())
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.CONFLICT)
			);
		}

		@Transactional
		@DisplayName("주문 ID가 존재하지 않는 경우, Not Found 에러가 발생한다.")
		@Test
		void failWithNotFound_whenOrderIdDoesNotExist() {
			// arrange
			UserEntity user = createUser();
			final Long nonExistentOrderId = -1L; // 존재하지 않는 주문 ID

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> orderFacade.payOrderByPoint(user.getLoginId(), nonExistentOrderId)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND)
			);
		}

		@Transactional
		@DisplayName("재고가 부족한 경우, 주문이 결제 실패 상태로 변경된다.")
		@Test
		void orderStatusIsPaymentFail_whenNotEnoughProductStock() {
			// arrange
			UserEntity user = createUser();
			ProductEntity product = createProduct();
			int quantity = 11; // 재고보다 많은 수량

			CreateOrderCommand command = CreateOrderCommand.of(
					UUID.randomUUID(),
					user.getLoginId(),
					List.of(
							CreateOrderItem.of(product.getId(), quantity)
					)
			);

			OrderInfo order = orderFacade.createOrder(command);

			// act
			OrderInfo orderInfo = orderFacade.payOrderByPoint(user.getLoginId(), order.id());

			// assert
			assertAll(
					() -> assertThat(orderInfo).isNotNull(),
					() -> assertThat(orderInfo.orderStatus()).isEqualTo(OrderStatus.PAYMENT_FAILED.name())
			);
		}

		@Transactional
		@DisplayName("주문의 상태가 PENDING이 아닌 경우, Conflict 에러가 발생해 실패한다.")
		@Test
		void failWithBadRequest_whenOrderStatusIsNotPending() {
			// arrange
			UserEntity user = createUser();
			ProductEntity product = createProduct();
			int quantity = 1;

			CreateOrderCommand command = CreateOrderCommand.of(
					UUID.randomUUID(),
					user.getLoginId(),
					List.of(
							CreateOrderItem.of(product.getId(), quantity)
					)
			);

			OrderInfo order = orderFacade.createOrder(command);
			orderJpaRepository.findById(order.id())
					.ifPresent(o -> o.updateStatus(OrderStatus.PAYMENT_CONFIRMED));

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> orderFacade.payOrderByPoint(user.getLoginId(), order.id())
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.CONFLICT)
			);
		}
	}
}
