package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.ResponseBinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/binary-contents")
@RequiredArgsConstructor
public class BinaryContentController
{
    private final BinaryContentService binaryContentService;

    @GetMapping("/{contentId}")
    public ResponseBinaryContentDto getBinaryContent(@PathVariable String contentId){
        return binaryContentService.findById(contentId);
    }
}
