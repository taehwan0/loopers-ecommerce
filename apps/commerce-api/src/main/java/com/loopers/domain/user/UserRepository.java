package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {

	Optional<UserEntity> findById(Long id);

	Optional<UserEntity> findByUserId(String userId);

	UserEntity save(UserEntity user);
}
