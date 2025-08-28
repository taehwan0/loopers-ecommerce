package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;

	public UserEntity getUserByLoginId(String loginId) {
		return userRepository.findByLoginId(loginId)
				.orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[loginId = " + loginId + "] 유저를 찾을 수 없습니다."));
	}

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
