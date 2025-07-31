package com.loopers.domain.brand;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BrandService {

	private final BrandRepository brandRepository;

	public Optional<BrandEntity> getBrand(Long id) {
		return brandRepository.findById(id);
	}
}
