package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.service.UserStatusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userstatus")
public class UserStatusController {
    final private UserStatusService userStatusService;
    public UserStatusController(UserStatusService userStatusService) {
        this.userStatusService = userStatusService;
    }
//
//    @PostMapping ("/{id}")
//    public String updateUserStatus(@GetMapping("id") {
//
//    }

}
