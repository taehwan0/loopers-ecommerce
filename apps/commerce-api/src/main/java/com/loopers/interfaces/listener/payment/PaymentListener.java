package com.loopers.interfaces.listener.payment;

import com.loopers.application.payment.PaymentEventHandler;
import com.loopers.payload.payment.PaymentFailEvent;
import com.loopers.payload.payment.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentListener {
	private final PaymentEventHandler paymentEventHandler;

	@EventListener
	public void handlePaymentSuccessEvent(PaymentSuccessEvent event) {
		paymentEventHandler.paymentSuccess(event);
	}

	@EventListener
	public void handlePaymentFailEvent(PaymentFailEvent event) {
		paymentEventHandler.paymentFail(event);
	}
}
