package com.loopers.domain.push;

import java.util.List;

// 주문(+결제) 성공 시 외부 알림을 보내기 위한 서비스
public interface PushService {

	void sendOrderSuccessPush(UserInfo user, OrderInfo order, PaymentInfo payment);

	record UserInfo(
			String loginId,
			String name
	) {

	}

	record OrderInfo(
			String orderStatus,
			List<OrderItemInfo> items
	) {

	}

	record OrderItemInfo(
			String productName,
			int quantity
	) {

	}

	record PaymentInfo(
			String paymentMethod,
			long amount
	) {

	}
}
