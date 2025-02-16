package com.sprint.mission;


import com.sprint.mission.entity.main.Message;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.service.MessageService;
import com.sprint.mission.service.UserService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class javaApplication {

    private final UserService userService;
    private final ChannelService channelService;
    private final MessageService messageService;

    public static void main(String[] args) {

        // 유저 생성 테스트
    }
}
