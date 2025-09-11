package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "product_metrics")
class ProductMetricsEntity(
    val productId: Long,
    var likeCount: Int = 0,
    var viewCount: Int = 0,
    var salesCount: Int = 0
) : BaseEntity() {

    init {
        require(productId > 0) { "productId는 0보다 커야합니다." }
        require(likeCount >= 0) { "likeCount는 0보다 커야합니다." }
        require(viewCount >= 0) { "viewCount는 0보다 커야합니다." }
        require(salesCount >= 0) { "salesCount는 0보다 커야합니다." }
    }

    fun increaseLikeCount() {
        likeCount++
    }

    fun decreaseLikeCount() {
        likeCount--
    }

    fun increaseViewCount() {
        viewCount++
    }

    fun increaseSalesCount() {
        salesCount++
    }
}
