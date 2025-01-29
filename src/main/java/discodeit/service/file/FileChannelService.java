package discodeit.service.file;

import discodeit.dto.ChannelInfoDto;
import discodeit.enity.Channel;
import discodeit.enity.ChannelType;
import discodeit.enity.User;
import discodeit.repository.file.FileChannelRepository;
import discodeit.service.ChannelService;

import java.util.*;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService {
    //싱글톤
    private static volatile FileChannelService instance;
    private final FileChannelRepository fileChannelRepository;
    private FileUserService fileUserService;
    private FileMessageService fileMessageService;

    public FileChannelService() {
        this.fileChannelRepository = new FileChannelRepository();
    }

    public static FileChannelService getInstance() {
        if (instance == null) {
            synchronized (FileChannelService.class) {
                if (instance == null) {
                    instance = new FileChannelService();
                }
            }
        }
        return instance;
    }

    protected void setService() {
        this.fileUserService = fileUserService.getInstance();
        this.fileMessageService = fileMessageService.getInstance();
    }

    @Override
    public UUID createChannel(String name, String description, ChannelType type) {
        Channel channel = new Channel(name, description, type);
        fileChannelRepository.save(channel);
        System.out.println("채널: " + name + "(" + type + ")" + ", 생성 시간: " + channel.getCreatedAt());
        return channel.getId();
    }

    // 채널 이름, 설명, 채널 내 사용자, 채널 내 메세지 조회
    @Override
    public ChannelInfoDto getChannelInfoById(UUID channelId) {
        try {
            Channel channel = findById(channelId);
            // 채널에 참여한 사용자 이름
            List<String> userNames = new ArrayList<>();
            for (UUID userId : channel.getUsers()) {
                userNames.add((fileUserService.findById(userId).getName()));
            }
            // 채널 내 메세지
            List<String> formattedMessages = fileMessageService.getMessagesByChannelId(channelId);

            return new ChannelInfoDto(channel.getName(), channel.getDescription(),
                    userNames, formattedMessages);
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 채널입니다. " + e.getMessage());
            return null;
        }
    }

    // 채널별 이름, 설명, 채널 내 사용자 조회
    @Override
    public List<ChannelInfoDto> getAllChannelsInfo() {
        Map<UUID, Channel> data = fileChannelRepository.findAll();
        if (data == null || data.isEmpty()) {
            System.out.println("채널이 없습니다.");
            return null;
        }
        List<ChannelInfoDto> list = new ArrayList<>();
        data.values().stream()
                .sorted(Comparator.comparing(channel -> channel.getCreatedAt()))
                .forEach(channel -> {
                    List<String> userNames = new ArrayList<>();
                    for (UUID userId : channel.getUsers()) {
                        userNames.add((fileUserService.findById(userId).getName()));
                    }
                    list.add(new ChannelInfoDto(channel.getName(), channel.getDescription(),
                            userNames, null));  // 메세지는 출력 x
                });
        return list;
    }


    @Override
    public void updateChannelName(UUID channelId, String name) {
        try {
            Channel channel = findById(channelId);
            String prevName = channel.getName();
            channel.updateName(name);
            // 수정 후 ser 파일에 저장
            fileChannelRepository.save(channel);
            System.out.println("채널 '" + prevName + "'이 '" + name + "'으로 변경되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 채널입니다. " + e.getMessage());
        }
    }

    @Override
    public void updateChannelDescription(UUID channelId, String description) {
        try {
            Channel channel = findById(channelId);
            channel.updateDescription(description);
            // 수정 후 ser 파일에 저장
            fileChannelRepository.save(channel);
            System.out.println("채널 설명이 변경되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 채널입니다. " + e.getMessage());
        }
    }

    @Override
    public void deleteChannel(UUID channelId) {
        try {
            // 채널에 속해있던 메세지도 삭제
            fileMessageService.deleteMessagesInChannel(channelId);
            fileChannelRepository.delete(channelId);
            System.out.println("채널이 삭제되었습니다.");
        } catch (NoSuchElementException e) {
            System.out.println("존재하지 않는 채널입니다. " + e.getMessage());
        }
    }

    @Override
    public void addUserIntoChannel(UUID channelId, UUID userId) {
        try {
            Channel channel = findById(channelId);
            User user = fileUserService.findById(userId);

            if (checkUserInChannel(userId, channelId)) {
                System.out.println(channel.getName() + "은 채널에 이미 입장한 사용자입니다.");
            } else {
                channel.getUsers().add(userId);
                // 채널 객체 수정 후 ser 파일에 반영
                fileChannelRepository.save(channel);
                System.out.println(user.getName() + "님이 채널 '"
                        + channel.getName() + "'에 입장합니다.");
            }
        } catch (NoSuchElementException e) {
            System.out.println("채널 또는 사용자 데이터가 올바르지 못합니다. " + e.getMessage());
        }
    }

    @Override
    public void deleteUserInChannel(UUID channelId, UUID userId) {
        try {
            Channel channel = findById(channelId);
            User user = fileUserService.findById(userId);

            if (!checkUserInChannel(userId, channelId)) {
                System.out.println(user.getName() + "은 채널에 없는 사용자입니다.");
            } else {
                channel.getUsers().remove(userId);
                // 채널 객체 수정 후 ser 파일에 반영
                fileChannelRepository.save(channel);
                System.out.println(user.getName() + "님이 채널 '"
                        + channel.getName() + "'에서 퇴장합니다.");
            }
        } catch (NoSuchElementException e) {
            System.out.println("채널 또는 사용자 데이터가 올바르지 못합니다. " + e.getMessage());
        }
    }

    @Override
    public void deleteUserInAllChannels(UUID userId) {
        Map<UUID, Channel> data = fileChannelRepository.findAll();
        if (data == null || data.size() == 0) return;
        data.values().stream()
                .forEach(channel -> {
                    channel.getUsers().remove(userId);
                    // 채널 객체 수정 후 ser 파일에 반영
                    fileChannelRepository.save(channel);
                });
    }

    @Override
    public Channel findById(UUID channelId) {
        return fileChannelRepository.findById(channelId);
    }

    @Override
    public List<Channel> getChannelsByUserId(UUID userId) {
        Map<UUID, Channel> data = fileChannelRepository.findAll();
        if (data == null || data.size() == 0) return null;
        List<Channel> channels = data.values().stream()
                .filter(channel -> channel.getUsers().contains(userId))
                .collect(Collectors.toList());
        return channels;
    }

    @Override
    public boolean checkUserInChannel(UUID userId, UUID channelId) {
        try {
            Channel channel = findById(channelId);
            // 채널이 유저를 포함하는지 반환
            return channel.getUsers().contains(userId);
        } catch (NoSuchElementException e) {
            throw e;
        }
    }
}
