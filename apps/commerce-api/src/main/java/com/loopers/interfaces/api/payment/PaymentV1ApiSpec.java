package com.loopers.interfaces.api.payment;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;

public interface PaymentV1ApiSpec {

	@Operation(
			summary = "포인트 결제",
			description = "사용자의 포인트로 결제합니다."
	)
	ApiResponse<PaymentV1Dto.PaymentResponse> paymentOrderByPoint(PaymentV1Dto.PointPaymentRequest request);

	@Operation(
			summary = "카드 결제",
			description = "사용자의 카드로 결제합니다."
	)
	ApiResponse<PaymentV1Dto.PaymentResponse> paymentOrderByCard(PaymentV1Dto.CardPaymentRequest request);
}
