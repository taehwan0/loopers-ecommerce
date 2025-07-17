package com.loopers.infrastructure.user;

import com.loopers.domain.user.UserEntity;
import com.loopers.domain.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository userJpaRepository;

	@Override
	public Optional<UserEntity> findById(Long id) {
		return userJpaRepository.findById(id);
	}

	@Override
	public Optional<UserEntity> findByUserId(String userId) {
		return userJpaRepository.findByUserId(userId);
	}

	@Override
	public UserEntity save(UserEntity user) {
		return userJpaRepository.save(user);
	}
}
