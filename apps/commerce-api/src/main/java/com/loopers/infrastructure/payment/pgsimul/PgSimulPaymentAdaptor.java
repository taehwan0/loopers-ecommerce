package com.loopers.infrastructure.payment.pgsimul;

import com.loopers.domain.payment.PaymentAdaptor;
import com.loopers.domain.payment.PaymentStatus;
import com.loopers.infrastructure.payment.pgsimul.PgSimulDTO.PaymentInfoResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PgSimulPaymentAdaptor implements PaymentAdaptor {

	private final PgSimulatorFeignClient pgSimulatorFeignClient;
	private final String clientId;
	private final String callbackUrl;

	public PgSimulPaymentAdaptor(
			PgSimulatorFeignClient pgSimulatorFeignClient,
			@Value("${pg.simul.client-id}") String clientId,
			@Value("${pg.simul.callback-url}") String callbackUrl
	) {
		this.pgSimulatorFeignClient = pgSimulatorFeignClient;
		this.clientId = clientId;
		this.callbackUrl = callbackUrl;
	}

	@CircuitBreaker(name = "pgSimulator", fallbackMethod = "fallbackRequestPayment")
	@Retry(name = "pgSimulator")
	@Override
	public PaymentResponse requestPayment(PaymentRequest request) {
		var paymentRequestDTO = new PgSimulDTO.PaymentRequest(
				request.orderId(),
				request.cardType().name(),
				request.cardNo().value(),
				request.amount(),
				callbackUrl
		);

		PgSimulApiResponse<PgSimulDTO.PaymentResponse> response = pgSimulatorFeignClient.requestPayment(clientId, paymentRequestDTO);

		return new PaymentResponse(response.data().transactionKey(), response.data().status());
	}

	private PaymentResponse fallbackRequestPayment(PaymentRequest request, Throwable t) {
		return new PaymentResponse(null, PaymentStatus.PENDING.name());
	}

	@Retry(name = "pgSimulator")
	@Override
	public void getPaymentInfo(String paymentTransactionKey) {
		PgSimulApiResponse<PaymentInfoResponse> response = pgSimulatorFeignClient.getPaymentInfoByTransactionKey(clientId, paymentTransactionKey);
		response.data().orderId();
		response.data().transactionKey();
		response.data().status();
	}

	@Retry(name = "pgSimulator")
	@Override
	public void getOrderPaymentInfo(String orderId) {
		var response = pgSimulatorFeignClient.getOrderPaymentInfo(
				clientId,
				orderId
		);

		response.data().orderId();
		response.data().transactions();

	}
}
