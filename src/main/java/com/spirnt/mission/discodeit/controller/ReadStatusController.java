package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.controller.swagger.ReadStatusApiDocs;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController implements ReadStatusApiDocs {

    private final ReadStatusService readStatusService;

    // 특정 채널의 메시지 수신 정보 생성
    @PreAuthorize("#request.userId() == authentication.principal.user.id")
    @PostMapping("")
    public ResponseEntity<ReadStatusDto> createReadStatus(
        @Valid @RequestBody ReadStatusCreateRequest request) {
        ReadStatusDto readStatus = readStatusService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
    }

    // 특정 채널의 메시지 수신 정보 수정
    @PreAuthorize("@authorizationEvaluator.isReadStatusOwner(#readStatusId, authentication.principal.user.id)")
    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatusDto> updateReadStatus(@PathVariable UUID readStatusId,
        @Valid @RequestBody ReadStatusUpdateRequest request) {
        ReadStatusDto readStatus = readStatusService.update(readStatusId, request);
        return ResponseEntity.status(HttpStatus.OK).body(readStatus);
    }

    // 특정 사용자의 메시지 수신 정보 조회
    /*
     * PathVariable -> RequestParam으로 변경
     * 리소스를 고유하게 식별하는 것보다는
     * 특정 userId에 대해 ReadStatus 데이터를 필터링한다고 봐야함
     * */
    @GetMapping("")
    public ResponseEntity<List<ReadStatusDto>> findAllByUserId(@RequestParam UUID userId) {
        List<ReadStatusDto> readStatuses = readStatusService.findAllByUserId(userId);
        return ResponseEntity.ok(readStatuses);
    }
}
