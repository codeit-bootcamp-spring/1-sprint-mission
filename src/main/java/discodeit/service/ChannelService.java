package discodeit.service;

import discodeit.enity.Channel;
import discodeit.enity.Message;
import discodeit.enity.User;

import java.util.UUID;

public interface ChannelService {
    // Create
    UUID createChannel(String name);    // 채널 생성

    // Read
    void viewAllChannels(); // 전체 채널 다건 조회

    void viewChannelInfo(UUID channelId);  // 채널 정보 단건 조회

    // Update
    void updateChannelName(UUID channelId, String name);   // 채널명 변경

    // Delete
    void deleteChannel(UUID channelId);    // 채널 삭제


    void addUserIntoChannel(UUID channelId, UUID userId);    // 유저가 채널에 입장

    void deleteUserInChannel(UUID channelId, UUID userId);   // 유저를 채널에서 삭제

    boolean validateChannel(UUID channelId);   // 실제 존재하는 채널인지 검증

}
