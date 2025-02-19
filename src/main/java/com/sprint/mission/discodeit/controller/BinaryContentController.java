package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ResultDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @GetMapping("/binaryContent/{binaryContentId}")
    public ResultDTO<BinaryContent> getBinaryContent(@PathVariable UUID binaryContentId){
        return ResultDTO.<BinaryContent>builder()
                .code(HttpStatus.OK.value())
                .message("메시지 수신정보 생성 완료")
                .data(binaryContentService.find(binaryContentId))
                .build();
    }

    @GetMapping("/binaryContent/{binaryContentIds}")
    public ResultDTO<List<BinaryContent>> getBinaryContent(@PathVariable List<UUID> ids){
        return ResultDTO.<List<BinaryContent>>builder()
                .code(HttpStatus.OK.value())
                .message("메시지 수신정보 생성 완료")
                .data(binaryContentService.findAllByIdIn(ids))
                .build();
    }
}
