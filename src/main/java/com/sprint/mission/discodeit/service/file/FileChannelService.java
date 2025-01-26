package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private static volatile FileChannelService instance;
    private final String FILE_NAME = "src/main/java/serialized/channels.ser";
    private final Map<UUID, Channel> data;

    private FileChannelService() {
        this.data = loadData();
    }

    protected static FileChannelService getInstance() {
        if (instance == null) {
            synchronized (FileChannelService.class) {
                if (instance == null) {
                    instance = new FileChannelService();
                }
            }
        }
        return instance;
    }

    @Override
    public Channel createChannel(String name, String topic, ChannelType type) {
        Channel channel = new Channel(name, topic, type);
        data.put(channel.getId(), channel);
        saveData();
        return channel;
    }

    @Override
    public Optional<Channel> getChannelDetails(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> findAllChannels() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean editChannel(UUID id, String name, String topic, ChannelType type) {
        boolean updated = data.computeIfPresent(id, (key, channel) -> {
            channel.update(name, topic, type);
            return channel;
        }) != null;
        if (updated) saveData();
        return updated;
    }

    @Override
    public boolean deleteChannel(UUID id) {
        boolean deleted = (data.remove(id) != null);
        if (deleted) saveData();
        return deleted;
    }

    private Map<UUID, Channel> loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ConcurrentHashMap<>();
        }
    }

    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
/*
구현체 비교
1.공통점(비즈니스 로직)
- 등록, 수정, 삭제 등 비즈니스 로직 동일
- sendMessage시 맨션된 사용자 파싱, 콘솔 알림 재사용
2.차이점(저장 로직)
- JCF*Service: 메모리에 ConcurrentHashMap으로 관리 -> 애플리케이션 종료 시 사라짐
- File*Service: 메모리에 있는 ConcurrentHashMap을 파일(.ser)로 직렬화(저장)하고, 재시작 시 역직렬화(로드) -> 영구 저장 가능
3.직렬화/역직렬화
- Serializable을 구현, 파일로부터 데이터를 읽고 쓸 때 ObjectInputStream / ObjectOutputStream 활용
- 변경사항이 있을 때마다 saveData() 호출로 파일에 반영

비즈니스 로직은 동일
저장 방식 : 인메모리 -> File IO로 교체 : 데이터 영속화
 */
