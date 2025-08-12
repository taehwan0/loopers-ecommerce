package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "point_account")
@Entity
public class PointAccountEntity extends BaseEntity {

	private Long userId;

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "point_balance", nullable = false))
	private Point pointBalance;

	private PointAccountEntity(Long userId, Point pointBalance) {
		if (userId == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "userId는 비어있을 수 없습니다.");
		}

		if (pointBalance == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "pointBalance는 비어있을 수 없습니다.");
		}

		if (pointBalance.isNegative()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "포인트 잔액은 음수가 될 수 없습니다.");
		}

		this.userId = userId;
		this.pointBalance = Point.of(pointBalance.getValue());
	}

	public void charge(Point point) {
		if (point == null || point.compareTo(Point.ZERO) <= 0) {
			throw new CoreException(ErrorType.BAD_REQUEST, "충전할 포인트는 비어있거나 0 이하일 수 없습니다.");
		}

		this.pointBalance = this.pointBalance.add(point);
	}

	public void deduct(Point point) {
		if (this.pointBalance.compareTo(point) < 0) {
			throw new CoreException(ErrorType.CONFLICT, "포인트 잔액이 부족합니다.");
		}

		this.pointBalance = this.pointBalance.subtract(point);
	}

	public static PointAccountEntity of(Long userId) {
		return new PointAccountEntity(userId, Point.ZERO);
	}
}
