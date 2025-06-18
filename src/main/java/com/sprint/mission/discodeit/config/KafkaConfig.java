package com.sprint.mission.discodeit.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;


/**
 * Kafka 토픽 자동 생성 및 관련 설정 관리
 */
@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${discodeit.kafka.topics.new-message}")
    private String newMessageTopic;

    @Value("${discodeit.kafka.topics.role-changed}")
    private String roleChangedTopic;

    @Value("${discodeit.kafka.topics.async-task-failed}")
    private String asyncTaskFailedTopic;

    @Value("${discodeit.kafka.topics.notification}")
    private String notificationTopic;

    @Bean
    public NewTopic newMessageTopic() {
        return TopicBuilder.name(newMessageTopic)
            .partitions(3)
            .replicas(1)
            .build();
    }

    @Bean
    public NewTopic roleChangedTopic() {
        return TopicBuilder.name(roleChangedTopic)
            .partitions(3)
            .replicas(1)
            .build();
    }

    @Bean
    public NewTopic asyncTaskFailedTopic() {
        return TopicBuilder.name(asyncTaskFailedTopic)
            .partitions(3)
            .replicas(1)
            .build();
    }

    @Bean
    public NewTopic notificationTopic() {
        return TopicBuilder.name(notificationTopic)
            .partitions(3)
            .replicas(1)
            .build();
    }
}
