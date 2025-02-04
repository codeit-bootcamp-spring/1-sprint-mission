package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.observer.manager.ObserverManager;
import com.sprint.mission.discodeit.validation.ChannelValidtor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FileChannelService implements ChannelService {

    private final ObserverManager observerManager;
    private final ChannelValidtor channelValidtor;
    private final HashMap<UUID, Channel> data = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(FileChannelService.class.getName());
    private final Path directory = Paths.get(System.getProperty("user.dir"), "Data/channel_data");
    private final String fileName = "channel_data.ser";

    public FileChannelService
            (ObserverManager observerManager,ChannelValidtor channelValidtor){
        this.observerManager = observerManager;
        this.channelValidtor = channelValidtor;
        init(directory);
        loadDataFromFile();
    }

    @Override
    public Channel createChannel(String chName){
        if(channelValidtor.isUniqueName(chName, getAllChannels())){
            Channel channel = new Channel(chName);
            data.put(channel.getChanneluuId(), channel);
            saveDataToFile();
            return channel;
        }
        throw new CustomException(ExceptionText.CHANNEL_CREATION_FAILED);
    }

    @Override
    public Channel getChannel(UUID uuid){
        return data.get(uuid);
    }

    @Override
    public Map<UUID, Channel> getAllChannels() {
        return new HashMap<>(data);
    }

    @Override
    public void updateChannel(UUID uuId, String name ){
        Channel channel = getChannel(uuId);
        if (channel != null) {
            channel.update(name);
        }
    }

    @Override
    public void deleteChannel(UUID uuid){
        data.remove(uuid);
        observerManager.channelDeletionEvent(uuid);// 채널 삭제 시 이름을 전달
    }

    // 디렉토리 초기화
    private void init(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 실패: " + e.getMessage());
            }
        }
    }

    private void saveDataToFile(){
        Path filePath = directory.resolve(fileName);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + e.getMessage());
        }
    }

    // 파일에서 직렬화된 객체를 불러오기
    private void loadDataFromFile() {
        Path filePath = directory.resolve(fileName);
        if (Files.exists(filePath)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
                Object readObject = ois.readObject();
                if (readObject instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<UUID, Channel> loadedData = (Map<UUID, Channel>) readObject;
                    data.putAll(loadedData);
                } else {
                    throw new RuntimeException("잘못된 데이터 형식");
                }
            } catch (IOException | ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, "파일 로드 실패", e);
            }
        }
    }

}
