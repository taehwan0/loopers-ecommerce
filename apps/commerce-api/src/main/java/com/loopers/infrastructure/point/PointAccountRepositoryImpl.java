package com.loopers.infrastructure.point;

import com.loopers.domain.point.PointAccountEntity;
import com.loopers.domain.point.PointAccountRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PointAccountRepositoryImpl implements PointAccountRepository {

	private final PointAccountJpaRepository pointAccountJpaRepository;

	@Override
	public PointAccountEntity save(PointAccountEntity pointAccount) {
		return pointAccountJpaRepository.save(pointAccount);
	}

	@Override
	public Optional<PointAccountEntity> findById(Long id) {
		return pointAccountJpaRepository.findById(id);
	}
}
