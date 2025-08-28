package com.loopers.interfaces.listener.dataflatform;

import com.loopers.application.dataflatform.DataFlatformEventHandler;
import com.loopers.domain.payment.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataFlatformListener {

	private final DataFlatformEventHandler dataFlatformEventHandler;

	@Async
	@EventListener
	public void handleDataFlatformEvent(PaymentEvent.PaymentSuccess event) {
		dataFlatformEventHandler.sendOrderSuccessPush(event);
	}
}
