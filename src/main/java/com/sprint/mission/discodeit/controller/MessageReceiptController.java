package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.MessageReceiptCreateRequest;
import com.sprint.mission.discodeit.dto.MessageReceiptResponse;
import com.sprint.mission.discodeit.dto.MessageReceiptUpdateRequest;
import com.sprint.mission.discodeit.service.MessageReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/message-receipts")
@RequiredArgsConstructor
public class MessageReceiptController {

    private final MessageReceiptService receiptService;

    // ✅ 1. 메시지 수신 정보 생성
    @PostMapping
    public ResponseEntity<Map<String, Object>> createReceipt(@RequestBody MessageReceiptCreateRequest createDTO) {
        log.info("📩 메시지 수신 정보 생성 요청 도착!");
        MessageReceiptResponse createdReceipt = receiptService.create(createDTO);

        // ✅ 응답 메시지와 생성된 수신 정보 포함
        Map<String, Object> response = new HashMap<>();
        response.put("message", "✅ 메시지 수신 정보 생성 완료");
        response.put("receipt", createdReceipt);

        return ResponseEntity.ok(response);
    }

    // ✅ 2. 특정 사용자의 메시지 수신 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<MessageReceiptResponse>> getUserReceipts(@PathVariable UUID userId) {
        log.info("📜 사용자 메시지 수신 정보 조회 요청: {}", userId);
        return ResponseEntity.ok(receiptService.getReceiptsByUser(userId));
    }

    // ✅ 3. 메시지 수신 정보 업데이트
    @PutMapping("/{receiptId}")
    public ResponseEntity<Map<String, String>> updateReceipt(
            @PathVariable UUID receiptId,
            @RequestBody MessageReceiptUpdateRequest updateDTO) {

        log.info("✏ 메시지 수신 정보 업데이트 요청: {}", receiptId);
        receiptService.update(receiptId, updateDTO);

        // ✅ 메시지 포함 응답 반환
        Map<String, String> response = new HashMap<>();
        response.put("message", "✅ 메시지 수신 정보 업데이트 완료");

        return ResponseEntity.ok(response);
    }
}
