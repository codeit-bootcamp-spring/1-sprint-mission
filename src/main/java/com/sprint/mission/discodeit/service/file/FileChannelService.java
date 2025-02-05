package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.domain.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.Setter;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileChannelService implements ChannelService {
    private final String FILE_NAME = "C:\\Users\\ypd06\\codit\\files\\channel.ser";
    private final Map<UUID, List<UUID>> messages = new HashMap<>(); //채널에서 보유 중인 메시지들 ( 변경 가능성 없음 )
    @Setter
    private FileMessageService fileMessageService;
    private static volatile FileChannelService instance;
    private final FileChannelRepository fileChannelRepository;
    private final ReadStatusRepository readStatusRepository;


    private FileChannelService(ReadStatusRepository readStatusRepository) {
        this.readStatusRepository = readStatusRepository;
        this.fileChannelRepository = new FileChannelRepository();
    }// Repository 한 번만 생성

    public static FileChannelService getInstance(ReadStatusRepository readStatusRepository) {
        if (instance == null) {
            synchronized (FileUserService.class) {
                if (instance == null) {
                    instance = new FileChannelService(readStatusRepository);
                }
            }
        }
        return instance;
    }

    public UUID addMessage_By_Channel(UUID channelId, UUID sender, String content) {
        if (!validationUUID(channelId) || sender == null) {
            System.out.println("channelId = " + channelId);
            System.out.println("sender = " + sender);
            System.out.println("채널이 없거나 작성자가 없습니다.!");
            return null;
        }
        System.out.println("채널에 메시지 생성 중");
        UUID messageId = fileMessageService.createMessage(sender, content);
        messages.get(channelId).add(messageId); //검증 필요
        return messageId;
    }
    public void deleteMessage_in_Channel(UUID messageId){
        messages.values().forEach(s -> s.remove(messageId));
        System.out.println("채널 속 메시지 삭제");
    }


    private boolean validationUUID(UUID channelId) {
        Channel findChannel = fileChannelRepository.findById(channelId);
        if (findChannel == null) { return false; }
        return true;
        //fileChannelRepository.loadFromSer(FILE_NAME).containsKey(channelId);
    }

    public List<Message> messages(UUID channelId) {
        if (!validationUUID(channelId)) {
            return null;
        }
        List<Message> collect = messages.get(channelId).stream().map(s -> fileMessageService.getMessage(s)).toList();
        return new ArrayList<>(collect);
    }

    @Override
    public ChannelDto createPrivateChannel(ChannelDto dto, List<UUID> userList) {
        Channel savedChannel = fileChannelRepository.save(dto.type());
        userList.forEach(userId -> readStatusRepository.save(new ReadStatus(userId, savedChannel.getId())));
        return new ChannelDto(savedChannel);
    }

    @Override
    public ChannelDto createPublicChannel(ChannelDto dto) {
        Channel savedChannel = fileChannelRepository.save(dto.name(), dto.type());

        //Map<UUID, Channel> channelMap = loadFromSer(FILE_NAME);
        /*if (channelMap.values().stream().anyMatch(channel -> channel.getChannelName().equals(channelName))) {
            //System.out.println("이미 존재하는 채널입니다.");
            Channel channel = channelMap.get(channelMap.keySet().stream().filter(s -> channelMap.get(s).getChannelName().equals(channelName)).findFirst().get());
            messages.put(channel.getChannelId(), new ArrayList<>());
            return channel.getChannelId();
        }*/
        //System.out.println("채널 생성 중");
        //Channel channel = new Channel(channelName);
        //channelMap.put(channel.getChannelId(), channel);
        if(!messages.containsKey(savedChannel.getId())) {
            messages.put(savedChannel.getId(), new ArrayList<>());
        }
        //saveToSer(FILE_NAME, channelMap);

        return new ChannelDto(savedChannel);
    }

    @Override
    public Channel getChannel(UUID id) {
        return fileChannelRepository.findById(id);
    }

    @Override
    public List<Channel> getChannels() {
        //List<Channel> collect = loadFromSer(FILE_NAME).values().stream().toList();
        List<Channel> collect = fileChannelRepository.findAll();
        return new ArrayList<>(collect);
    }

    @Override
    public void updateChannel(UUID id, String name) {
        fileChannelRepository.update(id, name);
        /*Map<UUID, Channel> channelMap = loadFromSer(FILE_NAME);
        if (channelMap.values().stream().anyMatch(channel -> channel.getChannelName().equals(name))) {
            System.out.println("이미 존재하는 채널입니다.");
            return;
        }

        if(channelMap.containsKey(id)){
            System.out.println("채널 수정 중");
            Channel channel = getChannel(id);
            channelMap.remove(channel.getChannelId());
            channel.update(name);
            channelMap.put(channel.getChannelId(), channel);
            saveToSer(FILE_NAME, channelMap);

        }else {
            System.out.println("채널을 찾을 수 없습니다.");
        }*/
        //리턴해서 출력하는 방안 고려
    }

    @Override
    public void deleteChannel(UUID channelId) {
        //Map<UUID, Channel> channelMap = loadFromSer(FILE_NAME);
        List<Channel> channels = fileChannelRepository.findAll();
        if(channels.contains(fileChannelRepository.findById(channelId))) {
            List<Message> collect = messages.get(channelId).stream().map(s -> fileMessageService.getMessage(s)).collect(Collectors.toList());
            for (Message message : collect) {
                try {
                    fileMessageService.deleteMessage(message.getId());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            fileChannelRepository.delete(channelId);
        }else {
            System.out.println("채널을 찾을 수 없습니다.");
        }
        /*
        if (channelMap.containsKey(channelId)) {
            //채널이 소유한 메시지
            List<Message> collect = messages.get(channelId).stream().map(s -> fileMessageService.getMessage(s)).collect(Collectors.toList());
            for (Message message : collect) {
                try {
                    fileMessageService.deleteMessage(message.getMessageId());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            channelMap.remove(channelId);
            saveToSer(FILE_NAME, channelMap);
        } else {

        }*/
    }

    /*private static void saveToSer(String fileName, Map<UUID, Channel> channelData) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(channelData); // 직렬화하여 파일에 저장
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<UUID, Channel> loadFromSer(String fileName) {
        Map<UUID, Channel> map = new HashMap<>();
        File file = new File(fileName);

        if (!file.exists() || file.length() == 0) {
            // 파일이 없거나 크기가 0이면 빈 Map 반환
            return map;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            map = (Map<UUID, Channel>) ois.readObject(); // 역직렬화
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return map;
    }
*/
}
