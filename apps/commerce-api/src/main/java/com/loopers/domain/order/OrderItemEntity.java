package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "order_item")
public class OrderItemEntity extends BaseEntity {

	private Long orderId;
	private Long productId;
	private int quantity;

	private OrderItemEntity(Long orderId, Long productId, int quantity) {
		if (orderId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문 ID는 비어있을 수 없습니다.");
		}

		if (productId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "상품 ID는 비어있을 수 없습니다.");
		}

		if (quantity <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "수량은 1이상이어야 합니다.");
		}

		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static OrderItemEntity of(Long orderId, Long productId, int quantity) {
		return new OrderItemEntity(orderId, productId, quantity);
	}

	public void updateQuantity(int quantity) {
		if (quantity <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "수량은 1이상이어야 합니다.");
		}
		this.quantity = quantity;
	}
}
