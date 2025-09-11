package com.loopers.domain.product


interface ProductMetricsRepository {
    fun findByProductId(productId: Long): ProductMetricsEntity?
    fun save(productMetrics: ProductMetricsEntity): ProductMetricsEntity
}
