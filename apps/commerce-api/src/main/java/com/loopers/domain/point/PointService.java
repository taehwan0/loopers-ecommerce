package com.loopers.domain.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PointService {

	private final PointAccountRepository pointAccountRepository;
	private final PointLedgerEntryRepository pointLedgerEntryRepository;

	public PointAccountEntity createPointAccount(Long userId) {
		return pointAccountRepository.save(PointAccountEntity.of(userId));
	}

	public PointAccountEntity getPointAccount(Long userId) {
		return pointAccountRepository.findById(userId)
				.orElseGet(() -> pointAccountRepository.save(PointAccountEntity.of(userId)));
	}

	public PointAccountEntity chargePoint(Long userId, Point point) {
		PointAccountEntity pointAccount = getPointAccount(userId);
		pointAccount.charge(point);

		pointLedgerEntryRepository.save(PointLedgerEntryEntity.of(pointAccount, PointLedgerEntryType.CHARGE, point));

		return pointAccount;
	}

	public PointAccountEntity deductPoint(Long userId, Point point) {
		PointAccountEntity pointAccount = getPointAccount(userId);
		pointAccount.deduct(point);

		pointLedgerEntryRepository.save(PointLedgerEntryEntity.of(pointAccount, PointLedgerEntryType.DEDUCT, point));

		return pointAccount;
	}
}
