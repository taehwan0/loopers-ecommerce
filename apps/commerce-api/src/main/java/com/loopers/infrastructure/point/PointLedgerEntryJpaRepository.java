package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointLedgerEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointLedgerEntryJpaRepository extends JpaRepository<PointLedgerEntryEntity, Long> {

}
