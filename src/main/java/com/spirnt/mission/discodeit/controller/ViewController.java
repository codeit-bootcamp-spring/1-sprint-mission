package com.spirnt.mission.discodeit.controller;

import com.spirnt.mission.discodeit.dto.message.MessageResponse;
import com.spirnt.mission.discodeit.dto.user.UserResponse;
import com.spirnt.mission.discodeit.service.MessageService;
import com.spirnt.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;

// 심화 요구사항 구현용
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/view")
public class ViewController {
    private final UserService userService;
    private final MessageService messageService;

    // 모든 사용자 조회 화면 서빙
    @RequestMapping(value = "/users/findAll", method = RequestMethod.GET)
    public String getAllUsersView(Model model) {
        List<UserResponse> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    // 특정 채널 내 메시지 목록 조회 화면 서빙
    // 파일 표시 잘 되는지 확인용
    @RequestMapping(value = "/messages/channel/{channelId}", method = RequestMethod.GET)
    public String getAllMessagesInChannel(Model model, @PathVariable UUID channelId) {
        // 임의의 User ID 세팅
        UUID userId = UUID.fromString("3b6ff632-372b-4e22-a363-6b8326797089");
        List<MessageResponse> messageResponses = messageService.findAllByChannelId(channelId, userId);
        model.addAttribute("messages", messageResponses);
        return "message-list";
    }
}
