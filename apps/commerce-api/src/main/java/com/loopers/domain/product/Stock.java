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
public class Stock {
	@Column(name = "quantity", nullable = false)
	private int quantity;

	private Stock(int quantity) {
		if (quantity < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "재고는 음수가 될 수 없습니다.");
		}

		this.quantity = quantity;
	}

	public static Stock of(int quantity) {
		return new Stock(quantity);
	}

	protected void decreaseQuantity(int quantity) {
		if (quantity < 0 ) {
			throw new CoreException(ErrorType.BAD_REQUEST, "0이하의 숫자를 입력할 수 없습니다.");
		}

		if (this.quantity - quantity < 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "재고는 음수가 될 수 없습니다."
			);
		}

		this.quantity -= quantity;
	}

	protected void increaseQuantity(int quantity) {
		if (quantity < 0 ) {
			throw new CoreException(ErrorType.BAD_REQUEST, "0이하의 숫자를 입력할 수 없습니다.");
		}

		this.quantity += quantity;
	}
}
