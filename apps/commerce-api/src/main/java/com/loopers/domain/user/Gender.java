package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;

public enum Gender {
	M,
	F,
	;

	public static Gender of(String value) {
		for (Gender gender: values()) {
			if (gender.name().equalsIgnoreCase(value)) {
				return gender;
			}
		}
		throw new CoreException(ErrorType.BAD_REQUEST, "잘못된 성별 입력입니다.");
	}
}
