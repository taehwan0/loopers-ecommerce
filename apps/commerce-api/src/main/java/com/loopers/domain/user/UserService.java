package com.loopers.domain.user;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;

	public Optional<UserEntity> findByUerId(String userId) {
		return userRepository.findByUserId(userId);
	}

	public UserEntity create(String userId, String name, Gender gender, String birth, String email) {
		UserEntity userEntity = new UserEntity(userId, name, gender, birth, email);
		return userRepository.save(userEntity);
	}

	public Optional<UserEntity> getUser(Long id) {
		return userRepository.findById(id);
	}
}
