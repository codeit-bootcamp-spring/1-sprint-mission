package discodeit.controller;

import discodeit.dto.readstatus.CreateReadStatusRequest;
import discodeit.entity.ReadStatus;
import discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/read-status")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ReadStatus> createReadStatus(@RequestBody CreateReadStatusRequest request) {
        return ResponseEntity.ok(readStatusService.create(request));
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatus>> getReadStatusByUserId(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }

    @RequestMapping(value = "/{channelId}", method = RequestMethod.PATCH)
    public ResponseEntity<List<ReadStatus>> updateReadStatus(@PathVariable("channelId") UUID channelId) {
        List<ReadStatus> readStatusList = readStatusService.findAll();
        readStatusList.stream().filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .forEach(readStatus -> readStatus.update(Instant.now()));
        return ResponseEntity.ok(readStatusList);
    }

}
