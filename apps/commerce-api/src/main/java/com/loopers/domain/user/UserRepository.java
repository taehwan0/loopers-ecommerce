package com.loopers.domain.user;

import java.util.Optional;

public interface UserRepository {

	Optional<UserEntity> findByUserId(String userId);

	UserEntity save(UserEntity user);
}
