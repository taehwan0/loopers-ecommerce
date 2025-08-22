package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.PaymentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {

	Optional<PaymentEntity> findByTransactionKey(String transactionKey);
}
