package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.service.domain.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/binaryContent/findById/{id}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContentDto> findBinaryContentById(@PathVariable("id") UUID id) {
        //BinaryContentDto binaryContentDto = binaryContentService.findById(id);
        return ResponseEntity.ok(binaryContentService.findById(id));
    }
    @RequestMapping(value = "/binaryContent/findByDomainId/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContentDto>> findBinaryContentByDomainId(@PathVariable("id") UUID id) {
        //List<BinaryContentDto> binaryContentDtos = binaryContentService.findByDomainId(id);
        return ResponseEntity.ok(binaryContentService.findByDomainId(id));
    }
    //Delete, update 과정 확인 필요
}
