package com.loopers.infrastructure.payment;

import com.loopers.domain.payment.PaymentEntity;
import com.loopers.domain.payment.PaymentRepository;
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
}
