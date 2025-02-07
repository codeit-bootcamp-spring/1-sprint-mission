package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {




    private static final String fileName = "savedata/channel.ser";
    private final Map<UUID, Channel> channelList;

    public FileChannelRepository() {
        this.channelList = load();
    }


    @Override
    public Channel save(Channel channel) {
        try (ObjectOutputStream oos = new ObjectOutputStream (new FileOutputStream(fileName))) {
            oos.writeObject(channelList);
            return channel;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패 : " + e.getMessage(), e);
        }
    }

    @Override
    public Channel findById(UUID id) {
        load();
        return channelList.get(id);
    }

    @Override
    public Map<UUID, Channel> load() {
        File file = new File(fileName);

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream
                (new FileInputStream(file)))
        {
            return (Map<UUID, Channel>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 로드 실패 : " + e.getMessage(), e);
        }

    }

    @Override
    public void delete(UUID id) {
        channelList.remove(id);
    }
}
