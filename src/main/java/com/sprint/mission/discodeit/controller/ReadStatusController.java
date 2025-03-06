package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.FindReadStatusResponseDto;
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
    public ResponseEntity<FindReadStatusResponseDto> create(@RequestBody CreateReadStatusRequestDto createReadStatusRequestDto) {
        FindReadStatusResponseDto findReadStatusResponseDto = readStatusService.create(createReadStatusRequestDto);

        return ResponseEntity.created(URI.create("/api/readStatus/" + findReadStatusResponseDto.getId())).body(findReadStatusResponseDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FindReadStatusResponseDto>> findAllByUserId(@PathVariable UUID userId) {
        List<FindReadStatusResponseDto> findReadStatusResponseDtoList = readStatusService.findAllByUserId(userId);

        return ResponseEntity.ok(findReadStatusResponseDtoList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FindReadStatusResponseDto> updateReadStatus(@PathVariable UUID id) {
        FindReadStatusResponseDto findReadStatusResponseDto = readStatusService.update(id);

        return ResponseEntity.ok(findReadStatusResponseDto);
    }
}
