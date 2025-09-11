package com.loopers.interfaces.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.loopers.Event
import com.loopers.EventType.ConsumerGroup
import com.loopers.EventType.Topic
import com.loopers.domain.product.ProductMetricService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class ProductMetricsV1Consumer(
    private val productMetricsService: ProductMetricService,
    private val objectMapper: ObjectMapper
) {

    @KafkaListener(
        topics = [Topic.PRODUCT_LIKE_CHANGED],
        groupId = ConsumerGroup.PRODUCT_METRICS_CONSUMER
    )
    fun consume(message: ConsumerRecord<String, String>, ack: Acknowledgment) {
        val e = Event.fromJson(message.value())

        productMetricsService.handleEvent(e)

        ack.acknowledge()
    }
}
