package com.loopers.interfaces.listener.payment;

import com.loopers.application.payment.PaymentEventHandler;
import com.loopers.domain.payment.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentListener {
	private final PaymentEventHandler paymentEventHandler;

	@EventListener
	public void handlePaymentSuccessEvent(PaymentEvent.PaymentSuccess event) {
		paymentEventHandler.paymentSuccess(event);
	}

	@EventListener
	public void handlePaymentSuccessEvent(PaymentEvent.PaymentFail event) {
		paymentEventHandler.paymentFail(event);
	}
}
