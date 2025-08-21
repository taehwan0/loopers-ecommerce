package com.loopers.infrastructure.payment.pgsimul;

import com.loopers.domain.payment.PaymentClient;
import com.loopers.infrastructure.payment.pgsimul.PgSimulDTO.PaymentInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PgSimulPaymentClient implements PaymentClient {

	private final PgSimulatorFeignClient pgSimulatorFeignClient;
	private final String clientId;
	private final String callbackUrl;

	public PgSimulPaymentClient(
			PgSimulatorFeignClient pgSimulatorFeignClient,
			@Value("${pg.simul.client-id}") String clientId,
			@Value("${pg.simul.callback-url}") String callbackUrl
	) {
		this.pgSimulatorFeignClient = pgSimulatorFeignClient;
		this.clientId = clientId;
		this.callbackUrl = callbackUrl;
	}


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

	@Override
	public void getPaymentInfo(String paymentTransactionKey) {
		PgSimulApiResponse<PaymentInfoResponse> response = pgSimulatorFeignClient.getPaymentInfoByTransactionKey(clientId, paymentTransactionKey);
		response.data().orderId();
		response.data().transactionKey();
		response.data().status();
	}

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
