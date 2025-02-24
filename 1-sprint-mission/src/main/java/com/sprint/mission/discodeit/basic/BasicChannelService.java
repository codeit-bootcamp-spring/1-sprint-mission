package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.DTO.*;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.etc.parse;
import com.sprint.mission.discodeit.repository.file.FileChannelService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JFCUserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/channel")
public class BasicChannelService extends parse {

    private final JCFChannelService channelService;
    private final UserStatusService userStatusService;
    private final ReadStatusService readStatusService;

    public BasicChannelService(@Qualifier("FileChannelService") JCFChannelService jcfChannelService,
                               @Qualifier("FileUserStatusService") UserStatusService userStatusService,
                                 @Qualifier("FileReadStatusService") ReadStatusService readStatusService){
        this.channelService= jcfChannelService;
        this.userStatusService=userStatusService;
        this.readStatusService=readStatusService;
    }



    @PostMapping()
    public void createNewChannel(@RequestParam String name) {
        channelService.createNewChannel(name);

    }
    @PostMapping("/private")
    public void createPrivateChannel() {
        channelService.createPrivateChannel();
    }

    @GetMapping("/name/{name}")
    public  List<ChannelDto> readChannel(@PathVariable String name) {
        return channelService.readChannel(name);
    }

    @GetMapping("/id/{id}")
    public  List<ChannelDto> readChannel(@PathVariable UUID id) {
        return channelService.readChannel(id);
    }

    @GetMapping("/all")
    public List<ChannelDto> readChannelAll() {
        return channelService.readChannelAll();
    }

    @PatchMapping("/name/id")
    public boolean updateChannelName(@RequestBody NameIdDto nameIdDto) {
        UUID id=nameIdDto.getId();
        String name= nameIdDto.getName();
        return channelService.updateChannelName(id,name);
    }

    @PostMapping("/name/username")
    public boolean updateChannelName(@RequestBody NameNameDto nameIdDto) {
        String ChannelName=nameIdDto.getName1();
        String name=nameIdDto.getName2();
        return channelService.updateChannelName(ChannelName,name);
    }

    @DeleteMapping("/id/{id}")
    public boolean deleteChannel(@PathVariable UUID id) {
        readStatusService.delete(channelService.getReadStatusAll(id));
        userStatusService.delete (channelService.getUserStatusAll(id));
        return channelService.deleteChannel(id);
    }

    @DeleteMapping("/name/{name}")
    public boolean deleteChannel(@PathVariable String name) {
        readStatusService.delete(channelService.getReadStatusAll(name));
        userStatusService.delete (channelService.getUserStatusAll(name));
        return channelService.deleteChannel(name);
    }

    @DeleteMapping("/user/{channelName}/{userName}")
    public void deleteUserToChannel(@PathVariable String channelName,@PathVariable String userName){
        channelService.deleteUserToChannel(channelName,userName);
    }



    @GetMapping("/user/{channelName}")
    public String readChannelInUser(@PathVariable String channelName) {
        return channelService.readChannelInUser(channelName);
    }

    @GetMapping("/message/{channelName}")
    public String readChannelInMessage(@PathVariable String channelName) {
        return channelService.readChannelInMessage(channelName);
    }

    @GetMapping("/username/channel/{userId}")
    public List<ChannelDto> findAllByUserId(@PathVariable UUID userId){
        return channelService.findAllByUserId(userId);
    }

    @GetMapping("/userid/channel/{userName}")
    public List<ChannelDto> findAllByUserName(@PathVariable String userName) {
        return channelService.findAllByUserName(userName);
    }


    @PatchMapping("/user")
    public  ReadStatus addUserToChannel(@RequestBody  RequestChannel request) {
        Object channelName=parseUUIDOrString(request.getChannel());
        Object userName=parseUUIDOrString(request.getUser());

        ReadStatus newReadStatus=channelService.addUserToChannel(channelName,userName);
        if(newReadStatus!=null)
            readStatusService.addReadStatus(newReadStatus);
        return newReadStatus;
    }

    @PostMapping("/message")
    public boolean sendMesgaeUserInChannel(@RequestBody SendMessaageInChannelDto sendMessaageInChannelDto){
        Object channel=parseUUIDOrString(sendMessaageInChannelDto.getChannel());
        Object sender = parseUUIDOrString(sendMessaageInChannelDto.getSender());
        Object receiver = parseUUIDOrString(sendMessaageInChannelDto.getReceiver());
        Object message = parseUUIDOrString(sendMessaageInChannelDto.getMessage());
        return channelService.sendMessageInUser(channel,sender,receiver,message);
    }



}
