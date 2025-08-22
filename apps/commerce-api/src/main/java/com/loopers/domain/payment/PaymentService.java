package com.loopers.domain.payment;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
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

	public PaymentEntity getByTransactionKey(String transactionKey) {
		return paymentRepository.findByTransactionKey(transactionKey)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[transactionKey = " + transactionKey + "] 결제 정보를 찾을 수 없습니다."));
	}
}
