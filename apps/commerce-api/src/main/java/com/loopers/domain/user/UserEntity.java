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

	@Column(name = "member_id", nullable = false, unique = true, updatable = false, length = 10)
	private String userId;
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

	private UserEntity(String userId, String name, Gender gender, String birth, String email) {
		UserValidator.validateBeforeCreateUser(userId, birth, email);

		this.userId = userId;
		this.name = name;
		this.gender = gender;
		this.birth = LocalDate.parse(birth);
		this.email = email;
		this.point = Point.of(0);
	}

	public static UserEntity of(String userId, String name, Gender gender, String birth, String email) {
		return new UserEntity(userId, name, gender, birth, email);
	}

	public void chargePoint(int pointValue) {
		this.point.addPoint(pointValue);
	}
}
