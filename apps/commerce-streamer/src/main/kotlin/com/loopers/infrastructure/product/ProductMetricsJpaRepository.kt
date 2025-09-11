package com.loopers.infrastructure.product

import com.loopers.domain.product.ProductMetricsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductMetricsJpaRepository: JpaRepository<ProductMetricsEntity, Long>
