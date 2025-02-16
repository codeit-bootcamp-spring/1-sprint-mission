package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.form.ChannelUpdateDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Component
@Slf4j
public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_PATH = "temp/channels-obj.dat";
    private final Map<UUID, Channel> data=new HashMap<>();

    @Override
    public void createChannel(UUID id, Channel channel) {
        data.put(id, channel);
        save();
    }

    @Override
    public void updateChannel(UUID id, ChannelUpdateDto channelUpdateDto) {
        Channel findChannel = data.get(id);
        if(findChannel.getChannelGroup().equals("PUBLIC")) {
            findChannel.setDescription(channelUpdateDto.getDescription());
            findChannel.setChannelName(channelUpdateDto.getChannelName());
            log.info("PUBLIC 채널 수정완료");
        }
        log.info("PRIVATE는 채널 수정 불가입니다.");
    }

    @Override
    public void deleteChannel(UUID id) {
        data.remove(id);
        save();
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        Map<UUID, Channel> loadChannels = load();
        return Optional.ofNullable(loadChannels.get(id));
    }

    @Override
    public Optional<Channel> findByChannelName(String channelName) {
        return Optional.empty();
    }

    @Override
    public List<Channel> findAll() {
        Map<UUID, Channel> loadChannels =load();
        return new ArrayList<>(loadChannels.values());
    }

    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        }catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다." + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<UUID, Channel> load() {
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(FILE_PATH))){
            return (Map<UUID, Channel>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다."+e.getMessage());
            return null;
        } catch (IOException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
