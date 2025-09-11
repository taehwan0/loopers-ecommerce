package com.loopers.domain.product

import com.loopers.Event
import com.loopers.EventType
import com.loopers.payload.EventPayload
import com.loopers.payload.like.ProductLikedEvent
import com.loopers.payload.like.ProductUnlikedEvent
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ProductMetricService(
    private val productMetricsRepository: ProductMetricsRepository,
) {

    @Transactional
    fun handleEvent(event: Event<EventPayload>) {
        when (event.eventType()) {
            EventType.PRODUCT_LIKED   -> {
                val productLikeEvent = (event.payload() as ProductLikedEvent)
                val productMetrics = productMetricsRepository.findByProductId(productLikeEvent.productId)
                    ?: ProductMetricsEntity(productId = productLikeEvent.productId)

                productMetrics.increaseLikeCount()

                productMetricsRepository.save(productMetrics)
            }
            EventType.PRODUCT_UNLIKED -> {
                val productUnlikeEvent = (event.payload() as ProductUnlikedEvent)
                val productMetrics = productMetricsRepository.findByProductId(productUnlikeEvent.productId)
                    ?: ProductMetricsEntity(productId = productUnlikeEvent.productId)

                productMetrics.decreaseLikeCount()

                productMetricsRepository.save(productMetrics)
            }
        }
    }
}
