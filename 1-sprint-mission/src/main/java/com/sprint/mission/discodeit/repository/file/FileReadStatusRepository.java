package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class FileReadStatusRepository {

    public void createStatus(UUID channelId, List<UUID> userList, UUID readUserId) throws IOException, ClassNotFoundException {
        ReadStatus readStatus = new ReadStatus();
        readStatus.setMessageList(new ArrayList<UUID>());
        readStatus.setChannelId(channelId);
        readStatus.setUsers(userList, readUserId);
        readStatus.setCreatedAt(Instant.now());

        saveStatus(readStatus);
    }

    public void addUser(UUID channelId, UUID userId) throws IOException, ClassNotFoundException {
        List<ReadStatus> statuses = getReadStatusListFromFile();
        for(ReadStatus readStatus : statuses){
            if(readStatus.getChannelId().equals(channelId)){
                readStatus.getCheckUserReadStatus().put(userId,true);
            }
        }
        modifyReadStatusOfFile(statuses);
    }

    public void deleteStatus(ChannelDto channelDto) throws IOException, ClassNotFoundException {
        List<ReadStatus> statuses = getReadStatusListFromFile();
        statuses.removeIf(status -> status.getChannelId().equals(channelDto.getChannelId()));
        modifyReadStatusOfFile(statuses);

    }

    public void deleteUser(UUID id) throws IOException, ClassNotFoundException {
        List<ReadStatus> statuses = getReadStatusListFromFile();
        for(ReadStatus readStatus : statuses){
            readStatus.getCheckUserReadStatus().remove(id);
        }
        modifyReadStatusOfFile(statuses);
    }

    public boolean checkUser(UUID channelId ,UUID id) throws IOException, ClassNotFoundException {
        List<ReadStatus> statuses = getReadStatusListFromFile();
        for(ReadStatus readStatus : statuses){
            if(readStatus.getChannelId().equals(channelId) && readStatus.getCheckUserReadStatus().containsKey(id)) return true;
        }

        return false;
    }

    public void findChannel(UUID channelId) throws IOException, ClassNotFoundException {
        List<ReadStatus> statuses = getReadStatusListFromFile();
        for(ReadStatus readStatus : statuses){
            if(readStatus.getChannelId().equals(channelId)){
                System.out.println(readStatus.getCheckUserReadStatus().keySet());
            }
        }
    }

    public List<ReadStatus> findStatus(UUID channelId) throws IOException, ClassNotFoundException {
        List<ReadStatus> statuses = getReadStatusListFromFile();
        List<ReadStatus> list = new ArrayList<>();
        for(ReadStatus readStatus : statuses){
            if(readStatus.getChannelId().equals(channelId)){
                list.add(readStatus);
            }
        }


        return list;
    }

    public Map<UUID,Boolean> findMessageStatus(UUID messageId, UUID channelId) throws IOException, ClassNotFoundException {
        List<ReadStatus> statuses = getReadStatusListFromFile();

        for(ReadStatus readStatus : statuses){
            if(readStatus.getChannelId().equals(channelId)){
                List<UUID> messages = readStatus.getMessageList();
                for(UUID id : messages){
                    if(id.equals(messageId)) return readStatus.getCheckUserReadStatus();
                }
            }
        }


        return null;
    }

    public List<UUID> findByUserId(UUID userId) throws IOException, ClassNotFoundException {
        List<ReadStatus> statuses = getReadStatusListFromFile();
        List<UUID> list = new ArrayList<>();

        for(ReadStatus readStatus : statuses){
            if(readStatus.getCheckUserReadStatus().containsKey(userId)) list.add(readStatus.getChannelId());
        }

        return list;
    }

    private void saveStatus(ReadStatus readStatus) throws IOException, ClassNotFoundException {
        List<ReadStatus> userStatuses = getReadStatusListFromFile();
        userStatuses.add(readStatus);

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("ReadStatus.ser"));
            out.writeObject(userStatuses);
            out.close();
        }
        catch (IOException ignored) {}
    }

    private List<ReadStatus> getReadStatusListFromFile() throws IOException, ClassNotFoundException {
        List<ReadStatus> readStatuses = new ArrayList<>();


        File file = new File("ReadStatus.ser");
        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                readStatuses = (List<ReadStatus>) in.readObject();
                in.close();
            }
        }

        return readStatuses;
    }

    private void modifyReadStatusOfFile(List<ReadStatus> readStatuses) throws IOException {
        try (
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("ReadStatus.ser"))) {
            out.writeObject(readStatuses);
            out.close();
        }
    }

    private boolean checkReadStatusId(UUID id) throws IOException, ClassNotFoundException {
        List<ReadStatus> statuses = getReadStatusListFromFile();
        for(ReadStatus status : statuses){
            if(status.getStatusId().equals(id)){
                return true;
            }
        }

        return false;
    }

    public void saveMessage(MessageDto messageDto) throws IOException, ClassNotFoundException {
        List<ReadStatus> statuses = getReadStatusListFromFile();
        for(ReadStatus status : statuses){
            if(status.getChannelId().equals(messageDto.getChannelId())){

                List<UUID> list;
                if(status.getMessageList() != null){
                   list = status.getMessageList();
                }
                else list = new ArrayList<>();

                list.add(messageDto.getMessageId());

                status.setMessageList(list);
                modifyReadStatusOfFile(statuses);
                return;
            }
        }

        throw new RuntimeException();
    }

    public void deleteMessage(MessageDto messageDto) throws IOException, ClassNotFoundException {
        List<ReadStatus> statuses = getReadStatusListFromFile();
        for(ReadStatus status : statuses){
            if(status.getChannelId().equals(messageDto.getChannelId())){

                List<UUID> list = status.getMessageList();;
                list.remove(messageDto.getMessageId());

                status.setMessageList(list);
                modifyReadStatusOfFile(statuses);
                return;
            }
        }

        throw new RuntimeException();
    }

    public void update(ChannelDto channelDto) throws IOException, ClassNotFoundException {
        List<ReadStatus> statuses = getReadStatusListFromFile();
        try {

            for(ReadStatus status : statuses){
                if(status.getChannelId().equals(channelDto.getChannelId())){
                    status.setUser(channelDto.getUserId());
                    modifyReadStatusOfFile(statuses);
                    return;
                }
            }

        } catch (RuntimeException e){
            throw new IOException();
        }

    }
}
