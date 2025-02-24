package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.global.response.ResultCode;
import com.sprint.mission.discodeit.global.response.ResultResponse;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/binary-content")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<ResultResponse<List<BinaryContent>>> findBinaryContents(
            @RequestBody List<UUID> binaryContentIdList) {
        return ResponseEntity.status(HttpStatus.OK)
                        .body(ResultResponse.of(ResultCode.BINARYCONTENT_LIST_FETCHED,
                                binaryContentService.findAllBinaryContentsById(binaryContentIdList)));
    }
}
