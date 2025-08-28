package com.loopers.infrastructure.push;

import com.loopers.domain.push.PushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MockPushService implements PushService {

	@Override
	public void sendOrderSuccessPush(UserInfo user, OrderInfo order, PaymentInfo payment) {
		log.info("Order Success. user: {}, order: {}, payment: {}", user, order, payment);
	}
}
