package some_path._1sprintmission.discodeit.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import some_path._1sprintmission.discodeit.dto.ChannelDTO;
import some_path._1sprintmission.discodeit.dto.ChannelUpdateDTO;
import some_path._1sprintmission.discodeit.dto.PrivateChannelDTO;
import some_path._1sprintmission.discodeit.entiry.Channel;
import some_path._1sprintmission.discodeit.service.ChannelService;

import java.util.UUID;

@RestController
@RequestMapping("/channel")
public class BasicChannelController {

    private final ChannelService channelService;

    public BasicChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PostMapping("create-private")
    public ResponseEntity CreatePrivateChannel(@RequestBody PrivateChannelDTO privateChannelDTO){
        Channel newPrivateChannel = channelService.createPrivateChannel(privateChannelDTO);
        return new ResponseEntity(newPrivateChannel, HttpStatus.OK);
    }

    @PostMapping("create-public")
    public ResponseEntity CreatePublicChannel(@RequestBody String channelName){
        Channel newPublicChannel = channelService.createPublicChannel(channelName);
        return new ResponseEntity(newPublicChannel, HttpStatus.OK);
    }

    @PostMapping("update-public")
    public ResponseEntity updatePublicChannel(@RequestBody ChannelUpdateDTO channelUpdateDTO){
        ChannelDTO updateChannel = channelService.update(channelUpdateDTO);
        return new ResponseEntity(updateChannel, HttpStatus.OK);
    }

    @DeleteMapping("delete")
    public ResponseEntity deleteChannel(@RequestBody UUID channelId){
        channelService.delete(channelId);
        return new ResponseEntity(HttpStatus.OK);
    }


}
