package com.loopers.interfaces.listener.coupon;

import com.loopers.application.coupon.CouponEventHandler;
import com.loopers.payload.payment.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CouponListener {

	private final CouponEventHandler couponEventHandler;

	@EventListener
	public void handlePaymentSuccessEvent(PaymentSuccessEvent event) {
		couponEventHandler.useCoupon(event);
	}
}
