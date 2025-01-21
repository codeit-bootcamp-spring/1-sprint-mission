package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {
    private static final String FILE_PATH = "channels.ser";

    @Override
    public void saveAll(List<Channel> channels) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(channels); // 직렬화하여 파일에 저장
        } catch (IOException e) {
            throw new RuntimeException("채널 목록 저장 중 오류 발생", e);
        }
    }

    @Override
    public List<Channel> loadAll() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<Channel>) ois.readObject(); // 파일에서 데이터 읽어오기
        } catch (EOFException e) {
            return new ArrayList<>(); // 파일이 비어있으면 빈 리스트 반환
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("채널 목록 읽기 중 오류 발생", e);
        }
    }

    @Override
    public void reset() {
        File file = new File(FILE_PATH);
        if (file.exists() && file.delete()) {
            System.out.println("채널 파일이 초기화되었습니다.");
        }
        saveAll(new ArrayList<>()); // 빈 리스트로 초기화
    }
}
