package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.UserRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DiscodeitApplication {
    static List<UserRequest> setupUser(UserService userService) {
        List<UserRequest> users = List.of(
                new UserRequest("JohnDoe", "P@ssw0rd1", "john.doe@example.com", "010-1234-5678",null),
                new UserRequest("JaneDoe", "P@ssw0rd2", "jane.doe@example.com", "010-5678-1234",123123123L),
                new UserRequest("Alice", "P@ssw0rd3", "alice.wonderland@example.com", "010-8765-4321", 456456456L),
                new UserRequest("Bob", "P@ssw0rd4", "bob.builder@example.com", "010-4321-8765", null),
                new UserRequest("Charlie", "P@ssw0rd5", "charlie.choco@example.com", "010-5678-9876", null)
        );

        for (UserRequest user : users) {
            userService.create(user);
        }
        return users;
    }
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);

        setupUser(ㅈ);

        System.out.println("==== User Read All ====");
        List<User> users = userService.readAll();
        users.forEach(user -> {
            System.out.println("userName : " + user.getUsername()
                    + " | Email : " + user.getEmail()
                    + " | phoneNumber : " + user.getPhoneNumber()
            );
        });




    }
}
