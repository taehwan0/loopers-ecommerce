package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {

	Optional<UserEntity> findById(Long id);

	Optional<UserEntity> findByLoginId(String loginId);

	UserEntity save(UserEntity user);
}
