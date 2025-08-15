package com.loopers.infrastructure.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopers.domain.product.ProductCacheRepository;
import com.loopers.domain.product.ProductEntity;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductRedisRepository implements ProductCacheRepository {

	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public Optional<ProductEntity> findById(Long id) {
		final String key = "product:" + id;

		Object object = redisTemplate.opsForValue().get(key);
		if (object == null) {
			return Optional.empty();
		} else {
			return Optional.of((ProductEntity) object);
		}
	}

	@Override
	public void save(ProductEntity product) {
		final String key = "product:" + product.getId();

		redisTemplate.opsForValue().set(key, product, Duration.ofMinutes(10));
	}
}
