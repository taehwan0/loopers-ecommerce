package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "point_ledger_entry")
@Entity
public class PointLedgerEntryEntity extends BaseEntity {
	@ManyToOne
	@JoinColumn(name = "point_account_id", nullable = false, updatable = false)
	private PointAccountEntity pointAccount;

	@Enumerated(EnumType.STRING)
	@Column(name = "entry_type", nullable = false)
	private PointLedgerEntryType entryType;

	@Embedded
	@AttributeOverride(name = "value", column = @Column(name = "point_amount", nullable = false))
	private Point amount;

	private PointLedgerEntryEntity(PointAccountEntity pointAccount, PointLedgerEntryType entryType, Point amount) {
		if (pointAccount == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "포인트 계정은 비어있을 수 없습니다.");
		}

		if (entryType == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "포인트 항목 유형은 비어있을 수 없습니다.");
		}

		if (amount == null) {
			throw new CoreException(ErrorType.BAD_REQUEST, "금액은 비어있을 수 없습니다.");
		}

		this.pointAccount = pointAccount;
		this.entryType = entryType;
		this.amount = amount;
	}

	public static PointLedgerEntryEntity of(PointAccountEntity pointAccount, PointLedgerEntryType entryType, Point amount) {
		return new PointLedgerEntryEntity(pointAccount, entryType, amount);
	}
}
