package com.loopers.domain.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentService {

	private final PaymentRepository paymentRepository;

	public PaymentEntity save(Long orderId, PaymentMethod method, Long amount) {
		PaymentEntity payment = PaymentEntity.of(orderId, method, amount);
		return paymentRepository.save(payment);
	}
}
