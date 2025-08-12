package com.loopers.domain.point;

import java.util.Optional;

public interface PointAccountRepository {

	PointAccountEntity save(PointAccountEntity pointAccount);

	Optional<PointAccountEntity> findById(Long id);
}
