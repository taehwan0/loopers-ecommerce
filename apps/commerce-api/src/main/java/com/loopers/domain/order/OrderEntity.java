package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "order")
// TODO: 배송 정보 및 결제 정보에 대한 내용은 추후 추가 필요
public class OrderEntity extends BaseEntity {

	@Column(name = "user_id", nullable = false, updatable = false)
	private Long userId;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	private OrderEntity(Long userId, OrderStatus orderStatus) {
		if (userId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문은 사용자에 속해야 합니다.");
		}

		if (orderStatus == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문 상태는 비어있을 수 없습니다.");
		}

		this.userId = userId;
		this.orderStatus = orderStatus;
	}

	public static OrderEntity of(Long userId) {
		return new OrderEntity(userId, OrderStatus.PENDING);
	}

	public void updateStatus(OrderStatus orderStatus) {
		if (orderStatus == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문 상태는 비어있을 수 없습니다.");
		}

		this.orderStatus = orderStatus;
	}
}
