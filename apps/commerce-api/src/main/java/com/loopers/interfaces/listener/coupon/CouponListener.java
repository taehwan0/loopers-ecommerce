package com.loopers.interfaces.listener.coupon;

import com.loopers.application.coupon.CouponEventHandler;
import com.loopers.domain.payment.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CouponListener {

	private final CouponEventHandler couponEventHandler;

	@EventListener
	public void handlePaymentSuccessEvent(PaymentEvent.PaymentSuccess event) {
		couponEventHandler.useCoupon(event);
	}
}
