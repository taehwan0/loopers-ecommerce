package com.loopers.interfaces.listener.dataplatform;

import com.loopers.application.dataplatform.DataPlatformEventHandler;
import com.loopers.domain.payment.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataPlatformListener {

	private final DataPlatformEventHandler dataPlatformEventHandler;

	@Async
	@EventListener
	public void handleDataPlatformEvent(PaymentEvent.PaymentSuccess event) {
		dataPlatformEventHandler.sendOrderSuccessPush(event);
	}
}
