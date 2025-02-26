package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.FindReadStatusResponseDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<FindReadStatusResponseDto> createReadStatus(@RequestParam UUID userId,
                                                                      @RequestParam UUID channelId) {
        CreateReadStatusRequestDto createReadStatusRequestDto = new CreateReadStatusRequestDto(userId, channelId);
        FindReadStatusResponseDto findReadStatusResponseDto = readStatusService.create(createReadStatusRequestDto);

        return ResponseEntity.created(URI.create("/api/readStatus/" + findReadStatusResponseDto.getId())).body(findReadStatusResponseDto);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
    public ResponseEntity<List<FindReadStatusResponseDto>> findReadStatus(@PathVariable UUID userId) {
        List<FindReadStatusResponseDto> findReadStatusResponseDtoList = readStatusService.findAllByUserId(userId);

        return ResponseEntity.ok(findReadStatusResponseDtoList);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<FindReadStatusResponseDto> updateReadStatus(@PathVariable UUID id) {
        FindReadStatusResponseDto findReadStatusResponseDto = readStatusService.update(id);

        return ResponseEntity.ok(findReadStatusResponseDto);
    }
}
