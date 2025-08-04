package com.loopers.domain.user;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;

	public Optional<UserEntity> findByLoginId(String loginId) {
		return userRepository.findByLoginId(loginId);
	}

	public UserEntity create(String loginId, String name, Gender gender, String birth, String email) {
		UserEntity userEntity = UserEntity.of(loginId, name, gender, birth, email);
		return userRepository.save(userEntity);
	}

	public Optional<UserEntity> getUser(Long id) {
		return userRepository.findById(id);
	}
}
