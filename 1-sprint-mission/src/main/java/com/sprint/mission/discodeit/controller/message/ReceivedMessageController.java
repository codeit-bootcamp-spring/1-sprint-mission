package com.sprint.mission.discodeit.controller.message;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponseDTO;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.interfacepac.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/received_messages")
public class ReceivedMessageController {
    private final ReadStatusService readStatusService;

    public ReceivedMessageController(ReadStatusService readStatusService) {
        this.readStatusService = readStatusService;
    }

    //특정 채널의 메시지 수신 정보 생성
    @PostMapping
    public ReadStatusResponseDTO createReadStatusMessage(@RequestBody ReadStatusCreateDTO createDTO){
        return readStatusService.create(createDTO);
    }

    //특정 채널의 메시지 수신 정보 수정
    @PatchMapping("/modify")
    public ReadStatusResponseDTO modifyReadStatusMessage(@RequestBody ReadStatusUpdateDTO updateDTO){
        return readStatusService.update(updateDTO);
    }

    //특정 사용자의 메시지 수신 정보 조회
    @GetMapping("/{userId}")
    public List<ReadStatusResponseDTO> getReadStatusUser(@PathVariable UUID userId){
        return readStatusService.findAllByUserId(userId);
    }



}
