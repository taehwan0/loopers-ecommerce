package com.loopers.interfaces.api.payment;

import com.loopers.application.order.CardPaymentCommand;
import com.loopers.application.order.PaymentInfo;
import com.loopers.application.order.PointPaymentCommand;
import com.loopers.application.payment.PaymentFacade;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.payment.PaymentV1Dto.CardPaymentRequest;
import com.loopers.interfaces.api.payment.PaymentV1Dto.PaymentResponse;
import com.loopers.interfaces.api.payment.PaymentV1Dto.PointPaymentRequest;
import com.loopers.support.RequireUserLoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
@RestController
public class PaymentV1Controller implements PaymentV1ApiSpec {

	private final PaymentFacade paymentFacade;

	@RequireUserLoginId
	@PostMapping("/point")
	@Override
	public ApiResponse<PaymentResponse> paymentOrderByPoint(
			@RequestBody PointPaymentRequest request
	) {
		PointPaymentCommand command = PointPaymentCommand.of(request.orderId());
		PaymentInfo paymentInfo = paymentFacade.paymentByPoint(command);

		return ApiResponse.success(PaymentResponse.from(paymentInfo));
	}

	@RequireUserLoginId
	@PostMapping("/card")
	@Override
	public ApiResponse<PaymentResponse> paymentOrderByCard(
			@RequestBody CardPaymentRequest request
	) {
		CardPaymentCommand command = CardPaymentCommand.of(request.orderId(), request.cardType(), request.cardNumber());
		PaymentInfo paymentInfo = paymentFacade.paymentByCard(command);

		return ApiResponse.success(PaymentResponse.from(paymentInfo));
	}
}
