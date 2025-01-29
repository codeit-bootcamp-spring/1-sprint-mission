package discodeit.repository.file;

import discodeit.enity.Channel;
import discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    @Override
    public void save(Channel channel) {
        // ser 파일에 Map으로 저장
        Map<UUID, Channel> channels = this.findAll();
        if (channels == null) {
            channels = new HashMap<>();
        }
        try (FileOutputStream fos = new FileOutputStream("channel.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            // 기존 Map을 읽어와 새로운 객체를 추가한 후 덮어씌움
            channels.put(channel.getId(), channel);
            oos.writeObject(channels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(UUID channelId) {
        Map<UUID, Channel> channels = this.findAll();
        if (channels == null || !channels.containsKey(channelId)) {
            throw new NoSuchElementException("Channel ID: " + channelId + " not found");
        }
        // 기존 Map을 읽어와 데이터를 삭제한 후 덮어씌움
        channels.remove(channelId);
        try (FileOutputStream fos = new FileOutputStream("channel.ser");
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(channels);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    @Override
    public Channel findById(UUID channelId) {
        Map<UUID, Channel> channels = findAll();
        Optional<Channel> finding_channel = channels.values().stream()
                .filter(channel -> channel.getId().equals(channelId))
                .findAny();
        return finding_channel
                .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " not found"));
    }

    public Map<UUID, Channel> findAll() {
        Map<UUID, Channel> channels = new HashMap<>();
        try (FileInputStream fis = new FileInputStream("channel.ser");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            channels = (Map<UUID, Channel>) ois.readObject();
        } catch (EOFException e) {
            return channels;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        return channels;
    }
}
