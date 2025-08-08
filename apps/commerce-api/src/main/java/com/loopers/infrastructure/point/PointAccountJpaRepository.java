package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointAccountJpaRepository extends JpaRepository<PointAccountEntity, Long> {

}
