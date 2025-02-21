package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.*;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/message-receipt")
@RequiredArgsConstructor
public class MessageReceiptController {
    // 메시지 수신 정보에 --> ReadStatus 가 포함된다.
    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ReadStatusCreateResponse> createReadStatus(@RequestBody ReadStatusCreateRequest readStatusCreateRequest){

        // createReadStatus가 하나의 기능만 담당하고 있다면 반환값을 response DTO로 변경하는데,
        // (1) BasicChannelService에 위치한 Private채널을 만드는 메서드에서, 생성되는 부분에서 반환된 엔티티를 사용하고 있고
        // (2) createReadStatus 에서는 response DTO를 전달하는 게 안전해서(또한 프로젝트 규칙을 정해서)
        // 일반적으로는 Serivce 레이어에서 DTO<->객체 변환이 일어나는 게 책임 분리 관점에서 깔끔하지만
        // 지금은 ReadStatus Entity를 컨트롤러 계층에 데리고와서 ...
        // response DTO에 각 값을 매칭한 후 ResponseEntity 로 전달합니다.
        // 좀 더 좋은 수정 방안이 있다면 알려주세요! 감사합니다

        ReadStatus readStatus = readStatusService.createReadStatus(readStatusCreateRequest);
        return ResponseEntity.ok(new ReadStatusCreateResponse(readStatus.getId(),
                                                            readStatus.getUserId(),
                                                            readStatus.getChannelId(),
                                                            readStatus.getLastMessageReadAt()));
    }


    @RequestMapping(method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateReadStatus(@RequestBody ReadStatusUpdateRequest readStatusUpdateRequest){
        readStatusService.updateReadStatus(readStatusUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStautsfindAllByUserIdResponse>> getReadStatusByUserId(@PathVariable UUID userId){
        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }

}
