package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.vo.Price;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payment_order")
// TODO: 배송 정보 및 결제 정보에 대한 내용은 추후 추가 필요
public class OrderEntity extends BaseEntity {

	@Column(name = "idempotency_key", nullable = false, unique = true, updatable = false)
	private UUID idempotencyKey;

	@Column(name = "user_id", nullable = false, updatable = false)
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_status", nullable = false)
	private OrderStatus orderStatus;

	@Column(name = "coupon_id", nullable = true)
	private Long couponId;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<OrderItemEntity> orderItems = new ArrayList<>();

	private OrderEntity(UUID idempotencyKey, Long userId, OrderStatus orderStatus, Long couponId) {
		if (idempotencyKey == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문은 idempotency key를 가져야 합니다.");
		}

		if (userId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문은 사용자에 속해야 합니다.");
		}

		if (orderStatus == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문 상태는 비어있을 수 없습니다.");
		}

		this.idempotencyKey = idempotencyKey;
		this.userId = userId;
		this.orderStatus = orderStatus;
		this.couponId = couponId;
	}

	public static OrderEntity of(UUID idempotencyKey, Long userId) {
		return new OrderEntity(idempotencyKey, userId, OrderStatus.PENDING, null);
	}

	public static OrderEntity of(UUID idempotencyKey, Long userId, Long couponId) {
		return new OrderEntity(idempotencyKey, userId, OrderStatus.PENDING, couponId);
	}

	public Price getTotalPrice() {
		Long totalAmount = this.orderItems.stream()
				.map(item -> item.getTotalPrice().getAmount())
				.reduce(0L, Long::sum);

		return Price.of(totalAmount);
	}

	public void updateStatus(OrderStatus orderStatus) {
		if (orderStatus == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문 상태는 비어있을 수 없습니다.");
		}

		this.orderStatus = orderStatus;
	}

	public void addItems(List<OrderItemEntity> orderItem) {
		this.orderItems.addAll(orderItem);
	}
}
