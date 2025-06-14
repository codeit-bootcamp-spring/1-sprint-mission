package com.sprint.mission.discodeit.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.TopicPartitionInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kafka 관리용 Admin Controller
 * Kafka 토픽 정보, 상태 관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/kafka")
@Tag(name = "Kafka Management", description = "Kafka 관리 API (어드민 전용)")
@PreAuthorize("hasRole('ADMIN')")
public class KafkaAdminController {

    private final KafkaAdmin kafkaAdmin;

    @Operation(summary = "Kafka 토픽 목록 조회")
    @GetMapping("/topics")
    public ResponseEntity<Map<String, Object>> getTopics() {
        try (AdminClient adminClient = AdminClient.create(
            kafkaAdmin.getConfigurationProperties())) {
            ListTopicsResult listTopicsResult = adminClient.listTopics();
            Set<String> topicNames = listTopicsResult.names().get();

            Map<String, Object> response = new HashMap<>();
            response.put("totalTopics", topicNames.size());
            response.put("topics", topicNames);

            log.info("Kafka 토픽 목록 조회 완료: 총 {}개", topicNames.size());
            return ResponseEntity.ok(response);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Kafka 토픽 목록 조회 실패", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve Kafka topics");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @Operation(summary = "특정 Kafka 토픽 상세 정보 조회")
    @GetMapping("/topics/{topicName}")
    public ResponseEntity<Map<String, Object>> getTopicDetails(@PathVariable String topicName) {
        try (AdminClient adminClient = AdminClient.create(
            kafkaAdmin.getConfigurationProperties())) {
            DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(
                Set.of(topicName));
            TopicDescription topicDescription = describeTopicsResult.values().get(topicName).get();

            Map<String, Object> response = new HashMap<>();
            response.put("name", topicDescription.name());
            response.put("internal", topicDescription.isInternal());
            response.put("partitions", topicDescription.partitions().size());

            Map<String, Object> partitionDetails = new HashMap<>();
            for (TopicPartitionInfo partitionInfo : topicDescription.partitions()) {
                Map<String, Object> partitionData = new HashMap<>();
                partitionData.put("partition", partitionInfo.partition());
                partitionData.put("leader", partitionInfo.leader().toString());
                partitionData.put("replicas", partitionInfo.replicas().size());
                partitionData.put("isr", partitionInfo.isr().size());
                partitionDetails.put("partition-" + partitionInfo.partition(), partitionData);
            }
            response.put("partitionDetails", partitionDetails);

            log.info("Kafka 토픽 상세 정보 조회 완료: {}", topicName);
            return ResponseEntity.ok(response);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Kafka 토픽 상세 정보 조회 실패: {}", topicName, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve topic details");
            errorResponse.put("topic", topicName);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @Operation(summary = "Discodeit 관련 토픽 목록 조회")
    @GetMapping("/discodeit-topics")
    public ResponseEntity<Map<String, Object>> getDiscodeitTopics() {
        try (AdminClient adminClient = AdminClient.create(
            kafkaAdmin.getConfigurationProperties())) {
            ListTopicsResult listTopicsResult = adminClient.listTopics();
            Set<String> allTopicNames = listTopicsResult.names().get();

            Set<String> discodeitTopics = allTopicNames.stream()
                .filter(topicName -> topicName.startsWith("discodeit."))
                .collect(java.util.stream.Collectors.toSet());

            Map<String, Object> response = new HashMap<>();
            response.put("totalDiscodeitTopics", discodeitTopics.size());
            response.put("topics", discodeitTopics);

            log.info("Discodeit 관련 토픽 목록 조회 완료: 총 {}개", discodeitTopics.size());
            return ResponseEntity.ok(response);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Discodeit 관련 토픽 목록 조회 실패", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to retrieve Discodeit topics");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @Operation(summary = "Kafka 연결 상태 확인")
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> checkKafkaHealth() {
        try (AdminClient adminClient = AdminClient.create(
            kafkaAdmin.getConfigurationProperties())) {
            // 연결 테스트 - 클러스터 정보 조회
            adminClient.describeCluster().clusterId().get();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "healthy");
            response.put("message", "Kafka connection is active");

            log.info("Kafka 연결 상태 확인 완료: 정상");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Kafka 연결 상태 확인 실패", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "unhealthy");
            errorResponse.put("message", "Failed to connect to Kafka");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}