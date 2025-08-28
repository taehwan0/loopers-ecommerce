package com.loopers.application.coupon;

import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.coupon.UserCouponEntity;
import com.loopers.domain.order.OrderEntity;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CouponEventHandler {

	private final OrderService orderService;
	private final CouponService couponService;

	@Transactional
	public void useCoupon(PaymentEvent.PaymentSuccess event) {
		OrderEntity order = orderService.getOrder(event.orderId());
		if (order.getCouponId() != null) {
			UserCouponEntity coupon = couponService.getUserCouponById(order.getCouponId());
			coupon.use();
		}
	}
}
