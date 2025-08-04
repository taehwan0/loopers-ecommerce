package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "order_item")
public class OrderItemEntity extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false, updatable = false)
	private OrderEntity order;

	@Column(name = "product_id", nullable = false, updatable = false)
	private Long productId;

	@Column(name = "quantity", nullable = false)
	private int quantity;


	private OrderItemEntity(OrderEntity order, Long productId, int quantity) {
		if (order == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문은 비어있을 수 없습니다.");
		}

		if (productId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "상품 ID는 비어있을 수 없습니다.");
		}

		if (quantity <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "수량은 1이상이어야 합니다.");
		}

		this.order = order;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static OrderItemEntity of(OrderEntity order, Long productId, int quantity) {
		return new OrderItemEntity(order, productId, quantity);
	}
}
