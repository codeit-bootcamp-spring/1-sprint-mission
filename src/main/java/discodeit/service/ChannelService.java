package discodeit.service;

import discodeit.enity.Channel;
import discodeit.enity.Message;
import discodeit.enity.User;

public interface ChannelService {
    // Create
    Channel createChannel(String name);    // 채널 생성
    // Read
    void viewAllChannels(); // 전체 채널 다건 조회
    void viewChannelInfo(Channel channel);  // 채널 정보 단건 조회
    // Update
    void updateChannelName(Channel channel, String name);   // 채널명 변경
    // Delete
    void deleteChannel(Channel channel);    // 채널 삭제


    void addUserIntoChannel(Channel channel, User user);    // 유저가 채널에 입장
    void deleteUserInChannel(Channel channel, User user);   // 유저를 채널에서 삭제

}
