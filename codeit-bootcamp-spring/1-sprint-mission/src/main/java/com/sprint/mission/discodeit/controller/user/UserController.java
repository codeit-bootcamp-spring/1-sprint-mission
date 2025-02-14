package com.sprint.mission.discodeit.controller.user;

import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/{id}")
    public void testUUid(
            @PathVariable(name = "id") UUID userId
    ) {
        System.out.println("userId = " + userId);
    }
}
