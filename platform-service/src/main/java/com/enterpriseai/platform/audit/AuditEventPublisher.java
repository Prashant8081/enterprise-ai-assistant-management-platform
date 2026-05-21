package com.enterpriseai.platform.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AuditEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AuditEventPublisher.class);

    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;
    private final String topic;
    private final boolean enabled;

    public AuditEventPublisher(KafkaTemplate<String, AuditEvent> kafkaTemplate,
                               @Value("${app.kafka.audit-topic}") String topic,
                               @Value("${app.kafka.enabled:true}") boolean enabled) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
        this.enabled = enabled;
    }

    public void publish(AuditEvent event) {
        if (!enabled) {
            log.debug("Kafka audit publishing is disabled. auditId={}", event.auditId());
            return;
        }
        kafkaTemplate.send(topic, event.auditId().toString(), event)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.warn("Audit event could not be published to Kafka: {}", ex.getMessage());
                }
            });
    }
}
