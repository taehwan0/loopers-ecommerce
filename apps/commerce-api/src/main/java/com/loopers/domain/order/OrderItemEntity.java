package com.loopers.domain.order;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.vo.Price;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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

	@Embedded
	@AttributeOverride(name = "amount", column = @Column(name = "price_amount", nullable = false) )
	private Price price;

	@Embedded
	@AttributeOverride(name = "amount", column = @Column(name = "total_price_amount", nullable = false) )
	private Price totalPrice;

	private OrderItemEntity(OrderEntity order, Long productId, Price price, int quantity) {
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
		this.price = Price.of(price.getAmount());
		this.totalPrice = Price.of(price.getAmount() * quantity);
		this.quantity = quantity;
	}

	public static OrderItemEntity of(OrderEntity order, Long productId, Price price, int quantity) {
		return new OrderItemEntity(order, productId, price, quantity);
	}
}
