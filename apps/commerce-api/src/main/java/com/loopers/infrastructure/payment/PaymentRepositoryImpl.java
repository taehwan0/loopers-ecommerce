package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.PaymentEntity;
import com.loopers.domain.payment.PaymentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

	private final PaymentJpaRepository paymentJpaRepository;

	@Override
	public PaymentEntity save(PaymentEntity payment) {
		return paymentJpaRepository.save(payment);
	}

	@Override
	public Optional<PaymentEntity> findById(Long id) {
		return paymentJpaRepository.findById(id);
	}

	@Override
	public Optional<PaymentEntity> findByIdOrderId(Long orderId) {
		return paymentJpaRepository.findByOrderId(orderId);
	}

	@Override
	public Optional<PaymentEntity> findByTransactionKey(String transactionKey) {
		return paymentJpaRepository.findByTransactionKey(transactionKey);
	}
}
