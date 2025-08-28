package com.loopers.domain.payment;

import java.util.Optional;

public interface PaymentRepository {
	PaymentEntity save(PaymentEntity payment);

	Optional<PaymentEntity> findById(Long id);

	Optional<PaymentEntity> findByIdOrderId(Long orderId);

	Optional<PaymentEntity> findByTransactionKey(String transactionKey);
}
