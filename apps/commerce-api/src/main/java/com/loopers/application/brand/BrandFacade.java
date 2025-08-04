package com.loopers.application.brand;

import com.loopers.domain.brand.BrandEntity;
import com.loopers.domain.brand.BrandService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class BrandFacade {

	private final BrandService brandService;

	@Transactional(readOnly = true)
	public BrandInfo getBrand(Long id) {
		BrandEntity brandEntity = brandService.getBrand(id)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[id = " + id + "] 브랜드를 찾을 수 없습니다."));

		return BrandInfo.from(brandEntity);
	}
}
