package com.loopers.infrastructure.payment.pgsimul;


import com.loopers.infrastructure.payment.pgsimul.PgSimulDTO.OrderPaymentInfoResponse;
import com.loopers.infrastructure.payment.pgsimul.PgSimulDTO.PaymentInfoResponse;
import com.loopers.infrastructure.payment.pgsimul.PgSimulDTO.PaymentRequest;
import com.loopers.infrastructure.payment.pgsimul.PgSimulDTO.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "pg-simulator", url = "http://localhost:8082")
public interface PgSimulatorFeignClient {

	String X_USER_HEADER = "X-USER-ID";

	@PostMapping(value = "/api/v1/payments")
	PgSimulApiResponse<PaymentResponse> requestPayment(
			@RequestHeader(value = X_USER_HEADER, required = true) String userId,
			@RequestBody PaymentRequest request
	);

	@GetMapping(value = "/api/v1/payments/{transactionKey}")
	PgSimulApiResponse<PaymentInfoResponse> getPaymentInfoByTransactionKey(
			@RequestHeader(value = X_USER_HEADER, required = true) String userId,
			@PathVariable("transactionKey") String transactionKey
	);

	@GetMapping(value = "/api/v1/payments")
	PgSimulApiResponse<OrderPaymentInfoResponse> getOrderPaymentInfo(
			@RequestHeader(value = X_USER_HEADER, required = true) String userId,
			@RequestParam(value = "orderId", required = true) String orderId
	);
}
