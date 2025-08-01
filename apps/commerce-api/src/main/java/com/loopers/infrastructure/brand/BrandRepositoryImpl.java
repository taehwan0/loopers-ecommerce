package com.loopers.infrastructure.brand;

import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.brand.BrandRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BrandRepositoryImpl implements BrandRepository {

	private final BrandJpaRepository brandJpaRepository;

	@Override
	public Optional<BrandEntity> findById(Long id) {
		return brandJpaRepository.findById(id);
	}
}
