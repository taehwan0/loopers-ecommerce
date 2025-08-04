package com.loopers.domain.vo;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Price {
	@Column(name = "price_amount", nullable = false)
	private long amount;

	private Price(long amount) {
		if (amount < 0) {
			throw new CoreException(ErrorType.INTERNAL_ERROR, "가격은 음수가 될 수 없습니다.");
		}

		this.amount = amount;
	}

	public static Price of(long amount) {
		return new Price(amount);
	}
}
