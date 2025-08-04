package com.loopers.support.error.payment;

import com.loopers.support.error.ErrorType;
import lombok.Getter;

@Getter
public class PaymentException extends RuntimeException {

	private final ErrorType errorType;
	private final String customMessage;

	public PaymentException(ErrorType errorType, String customMessage) {
		super(customMessage != null ? customMessage : errorType.getMessage());
		this.errorType = errorType;
		this.customMessage = customMessage;
	}
}
