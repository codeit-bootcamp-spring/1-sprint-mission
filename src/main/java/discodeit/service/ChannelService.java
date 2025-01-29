package discodeit.service;

import discodeit.dto.ChannelInfoDto;
import discodeit.enity.Channel;
import discodeit.enity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    // Create
    UUID createChannel(String name, String description, ChannelType type);    // 채널 생성

    // Read
    List<ChannelInfoDto> getAllChannelsInfo(); // 전체 채널 다건 조회

    ChannelInfoDto getChannelInfoById(UUID channelId);  // 채널 정보 단건 조회

    // Update
    void updateChannelName(UUID channelId, String name);   // 채널명 변경

    void updateChannelDescription(UUID channelId, String description);     //채널 설명 변경

    // Delete
    void deleteChannel(UUID channelId);    // 채널 삭제


    void addUserIntoChannel(UUID channelId, UUID userId);    // 유저가 채널에 입장

    void deleteUserInChannel(UUID channelId, UUID userId);   // 유저를 채널에서 삭제

    void deleteUserInAllChannels(UUID userId);  // 유저를 모든 채널에서 삭제(유저 삭제를 위한 메소드)

    Channel findById(UUID channelId);

    List<Channel> getChannelsByUserId(UUID userId); //유저 아이디를 받아 그 유저가 소속된 채널을 반환

    boolean checkUserInChannel(UUID userId, UUID channelId);    // 유저가 채널에 소속되어 있는지 확인


}
