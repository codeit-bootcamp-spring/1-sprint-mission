package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.ResponseBinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/binary-contents")
@RequiredArgsConstructor
public class BinaryContentController
{
    private final BinaryContentService binaryContentService;

    @GetMapping("/{contentId}")
    public ResponseBinaryContentDto viewFile(@PathVariable String contentId) {
        return binaryContentService.findById(contentId);
    }

    @GetMapping
    //todo - 여러개 조회 수정하기
    public List<ResponseBinaryContentDto> getBinaryContents(@RequestParam List<String> contentIds){
        return binaryContentService.findAllByIdIn(contentIds);
    }

    @PostMapping
    public ResponseBinaryContentDto uploadBinaryContent(@RequestParam("file") MultipartFile file){
        return binaryContentService.create(file);
    }
}
