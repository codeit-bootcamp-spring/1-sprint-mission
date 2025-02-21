package com.sprint.mission.discodeit.test;

import com.sprint.mission.discodeit.dto.channelDto.CreatePublicChannelRequestDto;
import com.sprint.mission.discodeit.dto.channelDto.FindChannelResponseDto;
import com.sprint.mission.discodeit.dto.channelDto.UpdatePublicChannelRequestDto;
import com.sprint.mission.discodeit.service.ChannelMessageService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.view.DisplayChannel;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class ChannelTest {

    private static int channelIndex = 1;

    private final UserService userService;
    private final ChannelService channelService;
    private final ChannelMessageService channelMessageService;

    // 채널 생성
    public UUID setUpPublicChannel(UUID ownerId) throws IOException {

        String name = "test" + channelIndex;
        String explanation = "테스트용 채널" + (channelIndex++);

        CreatePublicChannelRequestDto channel = new CreatePublicChannelRequestDto(ownerId, name, explanation);

        UUID id = channelService.createPublic(channel);

        channelMessageService.getLastMessageTime(id);

        System.out.println("================================================================================");
        System.out.println("채널 생성 결과 : ");
        DisplayChannel.displayChannel(channelService.find(id), userService);
        System.out.println("================================================================================");

        return id;
    }

    // 채널 정보 수정
    public void updateChannel(UUID id) throws IOException {
        String category = "updateCategory";
        String name = "updateName";
        String explanation = "updateExplanation";

        UpdatePublicChannelRequestDto updatePublicChannelRequestDto = new UpdatePublicChannelRequestDto(id, category, name, explanation, true);

        channelService.updateChannel(updatePublicChannelRequestDto);
        channelMessageService.getLastMessageTime(id);

        System.out.println("================================================================================");
        System.out.println("채널 수정 결과 : ");
        DisplayChannel.displayChannel(channelService.find(id), userService);
        System.out.println("================================================================================");
    }

    // 채널 멤버 추가
    public void addMember(UUID id, UUID memberId) {

        FindChannelResponseDto channel = channelService.find(id);

        System.out.println("멤버 추가 전 :");
        DisplayChannel.displayChannel(channel, userService);

        channelService.addMember(id, memberId);
        channelMessageService.getLastMessageTime(id);
        channel = channelService.find(id);

        System.out.println("멤버 추가 후 :");
        DisplayChannel.displayChannel(channel, userService);
    }

    // 채널 멤버 삭제
    public void deleteMember(UUID id, UUID memberId) {

        channelMessageService.getLastMessageTime(id);
        FindChannelResponseDto channel = channelService.find(id);

        System.out.println("멤버 삭제 전 :");
        DisplayChannel.displayChannel(channel, userService);

        channelService.deleteMember(id, memberId);
        channel = channelService.find(id);

        System.out.println("멤버 삭제 후 :");
        DisplayChannel.displayChannel(channel, userService);
    }

    // 채널 삭제
    public void deleteChannel(UUID id, UUID userId) {

        channelMessageService.getLastMessageTime(id);

        System.out.println("================================================================================");
        System.out.println("채널 삭제 전 목록 :");
        DisplayChannel.displayAllChannel(channelService.findAllByUserId(userId), userService);
        System.out.println("================================================================================");

        channelService.delete(id);

        System.out.println("================================================================================");
        System.out.println("채널 삭제 후 목록 :");
        DisplayChannel.displayAllChannel(channelService.findAllByUserId(userId), userService);
        System.out.println("================================================================================");
    }
}