package com.loopers.interfaces.listener.coupon;

import com.loopers.application.coupon.CouponEventHandler;
import com.loopers.domain.payment.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class CouponListener {

	private final CouponEventHandler couponEventHandler;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handlePaymentSuccessEvent(PaymentEvent.PaymentSuccess event) {
		couponEventHandler.useCoupon(event);
	}
}
