package com.loopers.domain.brand;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "brand")
@Entity
public class BrandEntity extends BaseEntity {
	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "description")
	private String description;

	private BrandEntity(String name, String description) {
		if (name == null || name.isBlank()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "brand의 이름은 비어있을 수 없습니다.");
		}

		name = name.trim();
		if (name.length() > 255 ) {
			throw new CoreException(ErrorType.BAD_REQUEST, "brand의 이름은 255자 이내로만 가능합니다.");
		}

		this.name = name;
		this.description = description != null ? description.trim() : null;
	}

	public static BrandEntity of(String name, String description) {
		return new BrandEntity(name, description);
	}
}
