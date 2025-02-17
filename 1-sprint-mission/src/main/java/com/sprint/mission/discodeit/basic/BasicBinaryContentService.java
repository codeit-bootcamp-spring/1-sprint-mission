package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/binary")
public class BasicBinaryContentService {

    private final BinaryContentService binaryContentService;

    public BasicBinaryContentService(@Qualifier("FileBinaryContentService") BinaryContentService binaryContentService){
        this.binaryContentService=binaryContentService;
    }

    @GetMapping("/batch")
    public List<BinaryContent> findMultipleBinaryContents(@RequestParam List<UUID> ids) {
        return binaryContentService.findAllByIdIn(ids);
    }



}
