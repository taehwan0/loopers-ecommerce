package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class UserValidator {

	private static final Pattern USER_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9]{1,10}$");
	// email pattern은 약식으로 사용한다.
	// xx@yy.zz 수준이면 성공시킨다.
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
	private static final Pattern BIRTH_PATTERN = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}$");

	public static void validateBeforeCreateUser(String userId, String birth, String email) {
		validateUserId(userId);
		validateEmail(email);
		validateBirth(birth);
	}

	public static void validateUserId(String userId) {
		if (userId == null || !USER_ID_PATTERN.matcher(userId).matches()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "userId는 영문/숫자 10자 이내로만 가능합니다.");
		}
	}

	public static void validateEmail(String email) {
		if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "email 형식이 잘못되었습니다.");
		}
	}

	public static void validateBirth(String birth) {
		if (birth == null || !BIRTH_PATTERN.matcher(birth).matches()) {
			throw new CoreException(ErrorType.BAD_REQUEST, "생년월일 형식이 잘못되었습니다.");
		}

		try {
			LocalDate.parse(birth);
		} catch (DateTimeParseException e) {
			throw new CoreException(ErrorType.BAD_REQUEST, "생년월일 형식이 잘못되었습니다.");
		}
	}
}
