package com.loopers.domain.payment;

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
@Table(name = "payment")
public class PaymentEntity extends BaseEntity {
	@Column(name = "order_id", nullable = false, updatable = false)
	private Long orderId;

	@Column(name = "payment_method", nullable = false)
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;

	@Column(name = "amount", nullable = false)
	private long amount;

	@Column(name = "payment_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus = PaymentStatus.PENDING;

	@Column(name = "transaction_key", nullable = true)
	private String transactionKey;

	private PaymentEntity(Long orderId, PaymentMethod paymentMethod, long amount) {
		if (orderId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "주문 ID는 비어있을 수 없습니다.");
		}

		if (paymentMethod == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "결제 방법은 비어있을 수 없습니다.");
		}

		if (amount <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "결제 금액은 0보다 커야 합니다.");
		}

		this.orderId = orderId;
		this.paymentMethod = paymentMethod;
		this.amount = amount;
	}

	public static PaymentEntity of(Long orderId, PaymentMethod paymentMethod, long amount) {
		return new PaymentEntity(orderId, paymentMethod, amount);
	}

	public void success() {
		this.paymentStatus = PaymentStatus.SUCCESS;
	}

	public void fail() {
		this.paymentStatus = PaymentStatus.FAILURE;
	}

	public void setTransactionKey(String transactionKey) {
		if (this.transactionKey != null && transactionKey == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "거래 키는 비울 수 없습니다.");
		}

		this.transactionKey = transactionKey;
	}
}
