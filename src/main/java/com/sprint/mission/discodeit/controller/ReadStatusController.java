package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController implements ReadStatusApi {

    private final ReadStatusService readStatusService;

    @PostMapping
    public ResponseEntity<ReadStatusDto> create(@RequestBody CreateReadStatusRequestDto createReadStatusRequestDto) {
        ReadStatusDto readStatusDto = readStatusService.create(createReadStatusRequestDto);

        return ResponseEntity.created(URI.create("/api/readStatus/" + readStatusDto.getId())).body(readStatusDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ReadStatusDto>> findAllByUserId(@PathVariable UUID userId) {
        List<ReadStatusDto> readStatusDtoList = readStatusService.findAllByUserId(userId);

        return ResponseEntity.ok(readStatusDtoList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReadStatusDto> updateReadStatus(@PathVariable UUID id) {
        ReadStatusDto readStatusDto = readStatusService.update(id);

        return ResponseEntity.ok(readStatusDto);
    }
}
