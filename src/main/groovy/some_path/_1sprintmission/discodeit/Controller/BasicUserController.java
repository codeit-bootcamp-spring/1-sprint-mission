package some_path._1sprintmission.discodeit.Controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import some_path._1sprintmission.discodeit.dto.UserCreateRequestDTO;
import some_path._1sprintmission.discodeit.dto.UserDTO;
import some_path._1sprintmission.discodeit.dto.UserUpdateRequestDTO;
import some_path._1sprintmission.discodeit.entiry.User;
import some_path._1sprintmission.discodeit.entiry.UserStatus;
import some_path._1sprintmission.discodeit.service.UserService;
import some_path._1sprintmission.discodeit.service.UserStatusService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class BasicUserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    public BasicUserController(UserService userService, UserStatusService userStatusService) {
        this.userService = userService;
        this.userStatusService = userStatusService;
    }

    @PostMapping("/create")
    public ResponseEntity createUser(@RequestBody UserCreateRequestDTO userCreateRequestDTO){
        User createUser = userService.create(userCreateRequestDTO);
        return new ResponseEntity<>(createUser, HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity deleteUser(@RequestParam UUID userId){
        userService.delete(userId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity updateUser(@RequestBody UserUpdateRequestDTO userUpdateRequestDTO){
        UserDTO updateUser = userService.update(userUpdateRequestDTO);
        return new ResponseEntity(updateUser, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity getAllUser(){
        List<UserDTO> users = userService.findAll();
        return new ResponseEntity(users, HttpStatus.OK);
    }

    @PutMapping("/online")
    public ResponseEntity updateOnline(@RequestBody UUID userId, Instant newLeastSeenAt){
        UserStatus updateUserStatus = userStatusService.updateByUserId(userId, newLeastSeenAt);
        return new ResponseEntity(updateUserStatus, HttpStatus.OK);
    }
}
