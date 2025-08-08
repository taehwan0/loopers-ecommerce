package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointLedgerEntryEntity;
import com.loopers.domain.point.PointLedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PointLedgerEntryRepositoryImp implements PointLedgerEntryRepository {

	private final PointLedgerEntryJpaRepository pointLedgerEntryJpaRepository;

	@Override
	public PointLedgerEntryEntity save(PointLedgerEntryEntity pointLedgerEntry) {
		return pointLedgerEntryJpaRepository.save(pointLedgerEntry);
	}
}
