package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.ReadStatusReadDTO;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/read-status")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @Autowired
    public ReadStatusController(@Qualifier("basicReadStatusService") ReadStatusService readStatusService) {
        this.readStatusService = readStatusService;
    }

    // ✅ 1. 특정 채널의 메시지 수신 정보 생성
    @PostMapping
    public void createReadStatus(@RequestBody ReadStatusCreateDTO readStatusCreateDTO) {
        readStatusService.create(readStatusCreateDTO);
    }

    // ✅ 2. 특정 채널의 메시지 수신 정보 수정
    @PutMapping("/{id}")
    public void updateReadStatus(@PathVariable UUID id, @RequestBody ReadStatusUpdateDTO readStatusUpdateDTO) {
        readStatusService.update(id, readStatusUpdateDTO);
    }

    // ✅ 3. 특정 사용자의 메시지 수신 정보 조회
    @GetMapping("/users/{userId}")
    public List<ReadStatusReadDTO> getReadStatusByUser(@PathVariable UUID userId) {
        return readStatusService.readByUserId(userId);
    }
}
