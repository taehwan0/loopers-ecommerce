package com.loopers.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.OrderInfo;
import com.loopers.application.order.PaymentInfo;
import com.loopers.application.order.PlaceOrderCommand;
import com.loopers.application.order.PlaceOrderCommand.CreateOrderItem;
import com.loopers.application.order.PointPaymentCommand;
import com.loopers.application.payment.PaymentFacade;
import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointAccountEntity;
import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.Stock;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserEntity;
import com.loopers.domain.vo.Price;
import com.loopers.infrastructure.brand.BrandJpaRepository;
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

	@Autowired
	PaymentFacade paymentFacade;

	@MockitoSpyBean
	OrderService orderService;

	@Autowired
	BrandJpaRepository brandJpaRepository;

	@Autowired
	UserJpaRepository userJpaRepository;

	@Autowired
	ProductJpaRepository productJpaRepository;

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

	@DisplayName("주문 결제 테스트")
	@Nested
	class PlaceOrder {

		@DisplayName("주문 포인트 결제를 성공하면, 결제 완료 상태로 결과를 반환한다.")
		@Test
		void returnWithPaidStatus_whenOrderIsPaidByPoint() {
			// arrange
			UserEntity user = createUser();
			ProductEntity product = createProduct();
			createPointAccount(user, Point.of(10000L));
			int quantity = 1;

			PlaceOrderCommand command = PlaceOrderCommand.of(UUID.randomUUID(), user.getLoginId(), null, List.of(
					PlaceOrderCommand.CreateOrderItem.of(product.getId(), quantity)
			));

			// act
			OrderInfo orderInfo = orderFacade.placeOrder(command);
			PaymentInfo paymentInfo = paymentFacade.paymentByPoint(PointPaymentCommand.of(orderInfo.id()));

			// assert
			assertAll(
					() -> assertThat(paymentInfo).isNotNull(),
					() -> assertThat(paymentInfo.paymentStatus()).isEqualTo(PaymentStatus.SUCCESS.name())
			);
		}

		@Transactional
		@DisplayName("포인트가 부족하면, 결제 실패 상태로 반환된다.")
		@Test
		void failWithBadRequest_whenUserHasInsufficientPoints() {
			// arrange
			UserEntity user = createUser();
			ProductEntity product = createProduct();
			int quantity = 1;

			PlaceOrderCommand command = PlaceOrderCommand.of(
					UUID.randomUUID(),
					user.getLoginId(),
					null,
					List.of(PlaceOrderCommand.CreateOrderItem.of(product.getId(), quantity))
			);

			OrderInfo orderInfo = orderFacade.placeOrder(command);

			// act
			PaymentInfo paymentInfo = paymentFacade.paymentByPoint(PointPaymentCommand.of(orderInfo.id()));

			// assert
			assertAll(
					() -> assertThat(paymentInfo).isNotNull(),
					() -> assertThat(paymentInfo.paymentStatus()).isEqualTo(PaymentStatus.FAILURE.name())
			);
		}

		@Transactional
		@DisplayName("재고가 부족한 경우, Conflict 에러가 발생해 실패한다.")
		@Test
		void orderStatusIsPaymentFail_whenNotEnoughProductStock() {
			// arrange
			UserEntity user = createUser();
			ProductEntity product = createProduct();
			createPointAccount(user, Point.of(1000000L));
			int quantity = product.getStock().getQuantity() + 100; // 재고보다 많은 수량

			PlaceOrderCommand command = PlaceOrderCommand.of(
					UUID.randomUUID(),
					user.getLoginId(),
					null,
					List.of(CreateOrderItem.of(product.getId(), quantity))
			);

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> orderFacade.placeOrder(command)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.CONFLICT)
			);
		}

		@DisplayName("존재하지 않는 쿠폰 ID를 입력하면, Not Found 에러가 발생해 실패한다.")
		@Test
		void failWithNotFound_whenCouponIdIsNotExists() {
			// arrange
			UserEntity user = createUser();
			ProductEntity product = createProduct();
			createPointAccount(user, Point.of(1000000L));
			int quantity = 1;

			PlaceOrderCommand command = PlaceOrderCommand.of(
					UUID.randomUUID(),
					user.getLoginId(),
					-999L,
					List.of(CreateOrderItem.of(product.getId(), quantity))
			);

			// act
			CoreException exception = assertThrows(
					CoreException.class,
					() -> orderFacade.placeOrder(command)
			);

			// assert
			assertAll(
					() -> assertThat(exception).isNotNull(),
					() -> assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND)
			);
		}
	}
}
