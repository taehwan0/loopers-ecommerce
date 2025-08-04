package com.loopers.domain.user;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "member")
@Entity
public class UserEntity extends BaseEntity {

	@Column(name = "login_id", nullable = false, unique = true, updatable = false, length = 10)
	private String loginId;
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "gender", nullable = false)
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@Column(name = "birth", nullable = false)
	private LocalDate birth;
	@Column(name = "email", nullable = false)
	private String email;
	@Embedded
	private Point point;

	private UserEntity(String loginId, String name, Gender gender, String birth, String email) {
		UserValidator.validateBeforeCreateUser(loginId, birth, email);

		this.loginId = loginId;
		this.name = name;
		this.gender = gender;
		this.birth = LocalDate.parse(birth);
		this.email = email;
		this.point = Point.of(0);
	}

	public static UserEntity of(String loginId, String name, Gender gender, String birth, String email) {
		return new UserEntity(loginId, name, gender, birth, email);
	}

	public void chargePoint(long pointValue) {
		this.point.addPoint(pointValue);
	}

	public void debitPoints(long amount) {
		this.point.debitPoints(amount);
	}
}
