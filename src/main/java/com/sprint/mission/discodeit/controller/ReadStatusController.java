package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.service.domain.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;
    @RequestMapping(value = "/readStatus/find/{id}", method = RequestMethod.GET)
    public ResponseEntity<ReadStatusDto> findById(@PathVariable UUID id, Model model) {
        //ReadStatusDto findReadStatus = readStatusService.findById(id);
        //model.addAttribute("readStatus", findReadStatus);
        return ResponseEntity.ok(readStatusService.findById(id));
    }
    @RequestMapping(value = "/readStatus/findByUserId/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatusDto>> findByUserId(@PathVariable UUID id, Model model) {
        //List<ReadStatusDto> readStatuses = readStatusService.findAllByUserId(id);
        //model.addAttribute("readStatuses", readStatuses);
        return ResponseEntity.ok(readStatusService.findAllByUserId(id));
    }
    @RequestMapping(value = "/readStatus/update")
    public ResponseEntity<ReadStatusDto> update(@RequestParam UUID id, @RequestParam Instant createdAt, @RequestParam Instant updatedAt, @RequestParam UUID channelId, @RequestParam UUID userId, Instant lastReadAt, Model model) {
        ReadStatusDto before = readStatusService.findById(id);
        ReadStatusDto after = new ReadStatusDto(null, userId, channelId, lastReadAt, createdAt, updatedAt);
        readStatusService.update(before, after);
        return ResponseEntity.ok(readStatusService.findById(before.id()));
    }
    @RequestMapping(value = "/readStatus/delete/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteByUserId(@PathVariable UUID userId) {
        readStatusService.deleteByUserId(userId);
        return ResponseEntity.noContent().build();
    }
    @RequestMapping(value = "/readStatus/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteByReadStatusId(@PathVariable UUID id) {
        readStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @RequestMapping(value = "/readStatus/delete/{channelId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteByChannelIdId(@PathVariable UUID channelId) {
        readStatusService.deleteByChannelId(channelId);
        return ResponseEntity.noContent().build();
    }

}
