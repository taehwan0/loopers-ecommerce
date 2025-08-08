package com.loopers.domain.point;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Point {

	public static final Point ZERO = Point.of(BigDecimal.ZERO);

	@Column(name = "value", nullable = false)
	private BigDecimal value;

	private Point(BigDecimal value) {
		this.value = value;
	}

	public static Point of(BigDecimal value) {
		if (value == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "포인트 금액은 비어있을 수 없습니다.");
		}
		return new Point(value);
	}

	public static Point of(Long value) {
		if (value == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "포인트 금액은 비어있을 수 없습니다.");
		}
		return new Point(BigDecimal.valueOf(value));
	}

	public Point add(Point point) {
		if (point == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "추가할 포인트는 비어있을 수 없습니다.");
		}
		return Point.of(this.value.add(point.getValue()));
	}

	public Point subtract(Point point) {
		if (point == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "차감할 포인트는 비어있을 수 없습니다.");
		}
		return Point.of(this.value.subtract(point.getValue()));
	}

	public boolean isNegative() {
		return this.value.compareTo(BigDecimal.ZERO) < 0;
	}

	public int compareTo(Point other) {
		if (other == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "비교할 포인트는 비어있을 수 없습니다.");
		}
		return this.value.compareTo(other.getValue());
	}
}
