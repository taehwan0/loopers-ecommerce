package com.loopers.infrastructure.product

import com.loopers.domain.product.ProductMetricsEntity
import com.loopers.domain.product.ProductMetricsRepository
import org.springframework.stereotype.Repository

@Repository
class ProductMetricsRepositoryImp(
    private val productMetricsJpaRepository: ProductMetricsJpaRepository
) : ProductMetricsRepository {

    override fun findByProductId(productId: Long): ProductMetricsEntity? {
        return productMetricsJpaRepository.findById(productId).orElse(null)
    }

    override fun save(productMetrics: ProductMetricsEntity): ProductMetricsEntity {
        return productMetricsJpaRepository.save(productMetrics)
    }
}
