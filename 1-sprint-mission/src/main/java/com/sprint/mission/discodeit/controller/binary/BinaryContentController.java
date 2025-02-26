package com.sprint.mission.discodeit.controller.binary;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDTO;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
public class BinaryContentController {
    final BinaryContentService binaryContentService;

    public BinaryContentController(BinaryContentService binaryContentService) {
        this.binaryContentService = binaryContentService;
    }

    //binaryId로 binaryContentDTO 찾기 -> messageId & fileData 포함 되어 있는 DTO
    @GetMapping("/find")
    public BinaryContentDTO getBinaryContent(@RequestParam("binaryContentId") UUID binaryContentId) {
        return binaryContentService.find(binaryContentId);
    }

    //userId로 binaryResponseContentDTO 찾기-> messageId & fileData 포함 X DTO
    @GetMapping("/list/{userId}")
    public List<BinaryContentResponseDTO> getBinaryContentsUserById(@PathVariable UUID userId) {
        return binaryContentService.findAllByUserId(userId);
    }



}
