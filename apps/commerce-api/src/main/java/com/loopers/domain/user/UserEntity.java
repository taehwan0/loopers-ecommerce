package com.loopers.domain.user;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "member")
@Entity
public class UserEntity extends BaseEntity {

	@Column(name = "user_id", nullable = false, unique = true, updatable = false)
	private String userId;
	private String name;
	private Gender gender;
	private LocalDate birth;
	private String email;

	public UserEntity(String userId, String name, Gender gender, String birth, String email) {
		UserValidator.validateBeforeCreateUser(userId, birth, email);

		this.userId = userId;
		this.name = name;
		this.gender = gender;
		this.birth = LocalDate.parse(birth);
		this.email = email;
	}
}
