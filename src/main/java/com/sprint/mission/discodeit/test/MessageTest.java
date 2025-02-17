package com.sprint.mission.discodeit.test;

import com.sprint.mission.discodeit.dto.messageDto.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.messageDto.FindMessageResponseDto;
import com.sprint.mission.discodeit.dto.messageDto.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.view.DisplayMessage;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class MessageTest {

    private static int messageIndex = 1;

    private final MessageService messageService;
    private final ChannelService channelService;
    private final UserService userService;

    // 메시지 생성
    public UUID setUpMessage(UUID channelId, UUID writerId) throws IOException {

        String context = "테스트용 메시지" + (messageIndex++);

        CreateMessageRequestDto message = new CreateMessageRequestDto(channelId, writerId, context, null);

        UUID id = messageService.create(message);

        System.out.println("================================================================================");
        System.out.println("메시지 생성 결과 : ");
        DisplayMessage.displayMessage(messageService.find(id), userService, channelService);
        System.out.println("================================================================================");

        return id;
    }
    
    // 메시지 수정
    public void updateMessage(UUID id) throws IOException {

        String context = "수정된 메시지";

        UpdateMessageRequestDto updateMessageRequestDto = new UpdateMessageRequestDto(id, context);

        messageService.updateContext(updateMessageRequestDto);

        System.out.println("================================================================================");
        System.out.println("메시지 수정 결과 : ");
        DisplayMessage.displayMessage(messageService.find(id), userService, channelService);
        System.out.println("================================================================================");
    }
    
    // 메시지 삭제
    public void deleteMessage(UUID id, UUID channelId) {

        System.out.println("================================================================================");
        System.out.println("메시지 삭제 전 목록 :");
        DisplayMessage.displayAllMessage(messageService.findAllByChannelId(channelId), userService, channelService);
        System.out.println("================================================================================");

        messageService.delete(id);

        System.out.println("================================================================================");
        System.out.println("메시지 삭제 후 목록 :");
        DisplayMessage.displayAllMessage(messageService.findAllByChannelId(channelId), userService, channelService);
        System.out.println("================================================================================");
    }
}
