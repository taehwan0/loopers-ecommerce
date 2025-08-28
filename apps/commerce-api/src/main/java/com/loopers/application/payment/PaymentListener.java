package com.loopers.application.payment;

import com.loopers.domain.payment.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentListener {
	private final PaymentFacade paymentFacade;

	@EventListener
	public void handlePaymentSuccessEvent(PaymentEvent.PaymentSuccess event) {
		paymentFacade.paymentSuccess(event);
	}

	@EventListener
	public void handlePaymentSuccessEvent(PaymentEvent.PaymentFail event) {
		paymentFacade.paymentFail(event);
	}
}
