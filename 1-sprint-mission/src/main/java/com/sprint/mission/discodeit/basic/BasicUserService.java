package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.DTO.message.MessageRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.etc.parse;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/user")
public class BasicUserService extends parse {

    private final UserService userService;


    public BasicUserService(@Qualifier("FileUserService") UserService userService) {
        this.userService = userService;
    }



    @PostMapping
    public void createNewUser(@RequestParam String name,@RequestParam String password,@RequestParam String email) {
        userService.createNewUser(name,password,email);
    }


    @GetMapping("/name/{userInfo}")
    public List<UserDTO> readUser(@PathVariable String userInfo) {
        return UserDTO.fromEntity(userService.readUser(userInfo));
    }

    @GetMapping("/id/{userInfo}")
    public List<UserDTO> readUser(@PathVariable UUID userInfo) {
        return UserDTO.fromEntity(userService.readUser(userInfo));
    }


    @GetMapping("/all")
    public List<UserDTO> readUserAll() {
        return UserDTO.fromEntity(userService.readUserAll());
    }


    @PatchMapping("/name/{name}")
    public boolean updateUserName(@PathVariable  String name, @RequestParam String change) {
        return userService.updateUserName(name,change);
    }

    @PatchMapping("/id/{id}")
    public boolean updateUserName(@PathVariable UUID id, @RequestParam String changeName) {
        return userService.updateUserName(id,changeName);
    }

    @DeleteMapping("/id/{id}")
    public boolean deleteUser(@PathVariable  UUID id,@RequestBody String password) {
        return userService.deleteUser(id,password);
    }

    @DeleteMapping("name/{name}")
    public boolean deleteUser(@PathVariable String name, @RequestBody String password) {
        return userService.deleteUser(name,password);
    }

    @PatchMapping("/img-to-name/{name}")
    public boolean updateUserSelfImg(@PathVariable  String name, @RequestBody String img) {
        char [] pass=img.toCharArray();
        return userService.updateUserSelfImg(name,pass);
    }

    @PatchMapping("/img-to-id/{name}")
    public boolean updateUserSelfImg(@PathVariable UUID id, @RequestBody String img) {
        char [] pass=img.toCharArray();
        return userService.updateUserSelfImg(id,pass);
    }

    @PostMapping("/send-message")
    public boolean sendMessageToUser(@RequestBody MessageRequest request) {
        Object sender = parseUUIDOrString(request.getSender());
        Object receiver = parseUUIDOrString(request.getReceiver());
        Object message = parseUUIDOrString(request.getMessage());
        return userService.sendMessageToUser(sender,receiver,message);
    }

    @PatchMapping("/isbool/{user}")
    public void updateUserIsBoolean(@PathVariable String user){
        Object changeUser=parseUUIDOrString(user);
        userService.updateUserStatus(changeUser);
    }





}
