package com.loopers.domain.product;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Price {
	@Column(name = "price_amount", nullable = false)
	long amount;

	private Price(long amount) {
		this.amount = amount;
	}

	public static Price of(long amount) {
		if (amount < 0) {
			throw new CoreException(ErrorType.INTERNAL_ERROR, "가격은 음수가 될 수 없습니다.");
		}

		return new Price(amount);
	}
}
