package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JcfUserService;

public class TestMain {
    public static void main(String[] args) {
        JcfUserService jcfUserService = new JcfUserService();

        User alice = new User("Alice", "alice@example.code");
        jcfUserService.create(alice);

        System.out.println("전부 조회: "+jcfUserService.readAll());

        alice.updateUsername("AliceUpdate");
        alice.updateEmail("alice_update@example.com");

        System.out.println("전부 조회: "+jcfUserService.readAll());

        jcfUserService.delete(alice.getId());

        System.out.println("전부 조회: "+jcfUserService.readAll());
    }
}
