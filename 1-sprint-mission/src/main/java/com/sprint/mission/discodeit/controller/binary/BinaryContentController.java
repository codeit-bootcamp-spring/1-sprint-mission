package com.sprint.mission.discodeit.controller.binary;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDTO;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("binary")
public class BinaryContentController {
    final BinaryContentService binaryContentService;

    public BinaryContentController(BinaryContentService binaryContentService) {
        this.binaryContentService = binaryContentService;
    }

    //binaryId로 binaryContentDTO 찾기 -> messageId & fileData 포함 되어 있는 DTO
    @GetMapping("/{binaryId}")
    public BinaryContentDTO getBinaryContent(@PathVariable UUID binaryId) {
        return binaryContentService.find(binaryId);
    }

    //userId로 binaryResponseContentDTO 찾기-> messageId & fileData 포함 X DTO
    @GetMapping("/list/{userId}")
    public List<BinaryContentResponseDTO> getBinaryContentsUserById(@PathVariable UUID userId) {
        return binaryContentService.findAllByUserId(userId);
    }



}
