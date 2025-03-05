//package com.sprint.mission.discodeit.service.file;
//
//import com.sprint.mission.discodeit.common.ErrorMessage;
//import com.sprint.mission.discodeit.common.UtilMethod;
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
//import com.sprint.mission.discodeit.service.ChannelService;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//public class FileChannelService implements ChannelService {
//    private final FileChannelRepository fileChannelRepository;
//    private final FileUserService fileUserService;
//
//    public FileChannelService(FileChannelRepository fileChannelRepository, FileUserService fileUserService) {
//        this.fileChannelRepository = fileChannelRepository;
//        this.fileUserService = fileUserService;
//    }
//
//    @Override
//    public Channel createChannel(UUID channelOwnerId, String name) {
//        User channelOwner = fileUserService.findUserByIdOrThrow(channelOwnerId);
//
//        Channel channel = Channel.of(name, channelOwner);
//
//        return fileChannelRepository.saveChannel(channel);
//    }
//
//    @Override
//    public Channel findChannelByIdOrThrow(UUID channelId) {
//        return Optional.ofNullable(fileChannelRepository.findChannelById(channelId))
//                .orElseThrow(() -> new RuntimeException(ErrorMessage.CHANNEL_NOT_FOUND.format(channelId)));
//    }
//
//    @Override
//    public List<Channel> findAllChannels() {
//        return fileChannelRepository.findAllChannels();
//    }
//
//    @Override
//    public Channel updateChannelName(UUID channelOwnerId, UUID channelId, String name) {
//        Channel foundChannel = findChannelByIdOrThrow(channelId);
//        fileUserService.findUserByIdOrThrow(channelOwnerId);
//
//        if (foundChannel.isNotOwner(channelOwnerId)) {
//            throw new RuntimeException(ErrorMessage.NOT_CHANNEL_CREATOR.format(channelOwnerId));
//        }
//
//        foundChannel.updateName(name);
//        foundChannel.updateUpdatedAt(UtilMethod.getCurrentTime());
//
//        return fileChannelRepository.saveChannel(foundChannel);
//    }
//
//    @Override
//    public void deleteChannel(UUID channelOwnerId, UUID channelId) {
//        // 채널 생성자 존재 유무 확인
//        fileUserService.findUserByIdOrThrow(channelOwnerId);
//        Channel foundChannel = findChannelByIdOrThrow(channelId);
//
//        if (foundChannel.isNotOwner(channelOwnerId)) {
//            throw new RuntimeException(ErrorMessage.NOT_CHANNEL_CREATOR.format(channelOwnerId));
//        }
//
//        // 채널 삭제
//        fileChannelRepository.removeChannel(channelId);
//    }
//
//    @Override
//    public Channel inviteUsers(UUID channelId, List<User> invitedUserList) {
//        Channel foundChannel = findChannelByIdOrThrow(channelId);
//
//        // 유저의 진위 여부에 대한 검증이 필요한가?
//
//        invitedUserList.forEach(user -> foundChannel.addChannelUser(user));
//
//        return fileChannelRepository.saveChannel(foundChannel);
//    }
//
//    @Override
//    public Channel leaveUsers(UUID channelId, List<User> leaveUserList) {
//        Channel foundChannel = findChannelByIdOrThrow(channelId);
//
//        leaveUserList.forEach(user -> foundChannel.deleteChannelUser(user));
//
//        return fileChannelRepository.saveChannel(foundChannel);
//    }
//}
