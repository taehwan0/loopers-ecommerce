package com.loopers.infrastructure.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loopers.domain.product.ProductQueryCacheRepository;
import com.loopers.domain.product.ProductSummary;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ProductQueryRedisRepository implements ProductQueryCacheRepository {

	private static final String PRODUCT_SUMMARY_CACHE_KEY_PREFIX = "product-summary:";

	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public Optional<ProductSummary> getProductSummary(Long productId) {
		final String key = PRODUCT_SUMMARY_CACHE_KEY_PREFIX + productId;
		String productSummaryStrings = redisTemplate.opsForValue().get(key);
		if (productSummaryStrings != null) {
			try {
				return Optional.ofNullable(objectMapper.readValue(productSummaryStrings, ProductSummary.class));
			} catch (JsonProcessingException e) {
				return Optional.empty();
			}
		}
		return Optional.empty();
	}

	@Override
	public void saveProductSummary(ProductSummary productSummary) {
		final String key = PRODUCT_SUMMARY_CACHE_KEY_PREFIX + productSummary.id();
		try {
			String productSummaryString = objectMapper.writeValueAsString(productSummary);
			redisTemplate.opsForValue().set(key, productSummaryString, Duration.ofMinutes(1));
		} catch (JsonProcessingException e) {
			log.error("ProductSummary 저장에 실패했습니다. productSummary: {}", productSummary);
		}
	}
}
