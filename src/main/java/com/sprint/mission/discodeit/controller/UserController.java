package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.UserCreateDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.domain.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;
    @GetMapping("/users/create")
    public String create() {
        return "user-form";
    }
    @RequestMapping(value = "/users/create", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UserDto> create( @RequestPart String name, @RequestPart String password, @RequestPart String email, @RequestPart(value = "profileImage", required = false) MultipartFile file) {
        UserDto userDto;
        if(file == null) {
            userDto = userService.createUser(new UserDto(name, password, email));
        }else {
            userDto = userService.createUser(new UserDto(name, password, email, new BinaryContentDto(file)));
        }
        System.out.println("userDto = " + userDto);

        //profile 확인 로그
        //BinaryContentDto binaryContentDto = binaryContentService.findById(userDto.binaryContentDto().id());
        //MultipartFile multipartFile = binaryContentDto.multipartFile();
        //System.out.println("multipartFile.getOriginalFilename() = " + multipartFile.getOriginalFilename());
        return ResponseEntity.ok(userDto);
    }

    @RequestMapping(value = "/users/find/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserCreateDto> findUser(@PathVariable UUID id, Model model) {
        UserDto user = userService.getUser(id);
        UserStatus userStatus = userStatusService.findByUserId(id);
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setUsername(user.userName());
        userCreateDto.setEmail(user.email());
        userCreateDto.setStatus(userStatus);
        //model.addAttribute("userList", userCreateDto);
        return ResponseEntity.ok(userCreateDto);
    }

    @RequestMapping(value = "/users/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserCreateDto>> findUsers(Model model) {
        List<UserDto> users = userService.getUsers();
        System.out.println("users = " + users);
        List<UserStatus> statuses = userStatusService.findAll();
        System.out.println("statuses = " + statuses);
        List<UserCreateDto> userCreateDtos = new ArrayList<>();
        for (UserDto userDto : users) {
            UserCreateDto userCreateDto = new UserCreateDto();
            userCreateDto.setUsername(userDto.userName());
            userCreateDto.setEmail(userDto.email());
            userCreateDto.setStatus(statuses.stream().filter(s->s.getUserId().equals(userDto.id())).findFirst().get());
            userCreateDtos.add(userCreateDto);
            //Profile
        }
        //model.addAttribute("userList", userCreateDtos);
        //return "user-list";
        return ResponseEntity.ok(userCreateDtos);
    }

    @RequestMapping(value = "/users/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@RequestParam UUID id, @RequestParam String name, @RequestParam String password, @RequestParam String email, Model model) {
        userService.updateUser(new UserDto(id, name, password, email));
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(value = "/users/update/userStatus", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateUserStatus(@RequestParam UUID id) {
        UserStatus userStatusByUserId = userStatusService.findByUserId(id);
        UserStatusDto updateUserStatus = new UserStatusDto(userStatusByUserId.getUserId(), userStatusByUserId.getUserId(), userStatusByUserId.getCreatedAt(), Instant.now(), null);
        //UserStatus 업데이트할 떄 UserDto를 수정할 수 있는 가능성이 있는데 이렇게 넘겨줘도 되는 지 모르겠다.
        userStatusService.update(new UserStatusDto(userStatusByUserId), updateUserStatus);
        return ResponseEntity.accepted().build();
    }
}
