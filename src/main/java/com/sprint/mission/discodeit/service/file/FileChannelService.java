package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.observer.manager.ObserverManager;
import com.sprint.mission.discodeit.validation.ChannelValidator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FileChannelService implements ChannelService {

    private final ObserverManager observerManager;
    private final ChannelValidator channelValidator;
    private final HashMap<UUID, Channel> data = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(FileChannelService.class.getName());
    private final Path directory = Paths.get(System.getProperty("user.dir"), "Data/channel_data");
    private final String fileName = "channel_data.ser";

    public FileChannelService
            (ObserverManager observerManager, ChannelValidator channelValidator){
        this.observerManager = observerManager;
        this.channelValidator = channelValidator;
        init(directory);
        loadDataFromFile();
    }

    @Override
    public Channel createPublicChannel(String name, String description){
        try {
            channelValidator.isUniqueName(name);
        }catch (CustomException e){
            System.out.println("Channel 생성 실패 -> "+ e.getMessage());
        }
        Channel ch1 = new Channel(ChannelType.PUBLIC,name, description);
        data.put(ch1.getId(), ch1);
        saveDataToFile();
        return ch1;
    }

    @Override
    public Channel createPrivateChannel(String name, String description){
        try {
            channelValidator.isUniqueName(name);
        }catch (CustomException e){
            System.out.println("Channel 생성 실패 -> "+ e.getMessage());
        }
        Channel ch1 = new Channel(ChannelType.PRIVATE,name, description);
        data.put(ch1.getId(), ch1);
        saveDataToFile();
        return ch1;
    }



    @Override
    public Channel find(UUID id){
        return data.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID uuId, String name,String description ){
        Channel channel = find(uuId);
        if (channel != null) {
            channel.update(name, description);
        }
        return null;
    }

    @Override
    public void delete(UUID uuid){
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
