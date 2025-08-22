package com.loopers.domain.payment;

import java.util.Optional;

public interface PaymentRepository {
	PaymentEntity save(PaymentEntity payment);

	Optional<PaymentEntity> findByTransactionKey(String transactionKey);
}
