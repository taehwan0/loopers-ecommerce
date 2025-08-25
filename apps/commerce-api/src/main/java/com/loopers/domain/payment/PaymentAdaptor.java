package com.loopers.domain.payment;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public interface PaymentAdaptor {

	PaymentResponse requestPayment(PaymentRequest request);

	void getPaymentInfo(String paymentTransactionKey);

	void getOrderPaymentInfo(String orderId);

	record PaymentRequest(
			String orderId,
			CardType cardType,
			CardNumber cardNo,
			long amount
	) {

		public static PaymentRequest of(String orderId, CardType cardType, CardNumber cardNo, long amount) {
			return new PaymentRequest(orderId, cardType, cardNo, amount);
		}

		public enum CardType {
			SAMSUNG,
			KB,
			HYUNDAI
			;

			public static CardType of(String value) {
				for (CardType cardType: values()) {
					if (cardType.name().equalsIgnoreCase(value)) {
						return cardType;
					}
				}
				throw new CoreException(ErrorType.BAD_REQUEST, "카드 타입이 올바르지 않습니다.");
			}
		}

		public record CardNumber(String value) {

			private static final String REGEX = "^\\d{4}-\\d{4}-\\d{4}-\\d{4}$";

			public CardNumber {
				if (value == null || !value.matches(REGEX)) {
					throw new CoreException(ErrorType.BAD_REQUEST, "카드 번호 형식이 잘못되었습니다. 올바른 형식은 'XXXX-XXXX-XXXX-XXXX'입니다.");
				}
			}

			public static CardNumber of(String value) {
				return new CardNumber(value);
			}
		}
	}

	record PaymentResponse(
			String transactionKey,
			String status
	) {

	}
}
