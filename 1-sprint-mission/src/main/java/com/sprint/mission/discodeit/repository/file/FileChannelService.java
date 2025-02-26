package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFReadStatusService;
import com.sprint.mission.discodeit.service.jcf.JFCUserStatusService;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

@Repository
@Service("FileChannelService")
public class FileChannelService extends JCFChannelService implements ChannelService, ChannelRepository {


    private final UserService jcUserService;
    private final JCFMessageService jcfMessageService;
    private final JCFReadStatusService jcfReadStatusService;
    private final JFCUserStatusService jfcUserStatusService;

    private static final String fileName = Paths.get("1-sprint-mission","1-sprint-mission","src", "main", "repo", "channel.txt").toString();

    public FileChannelService(@Qualifier("FileUserService") UserService jcUserService, @Qualifier("FileMessageService") JCFMessageService jcfMessageService, @Qualifier("FileReadStatusService") JCFReadStatusService jcfReadStatusService, @Qualifier("FileUserStatusService") JFCUserStatusService jfcUserStatusService){
        this.jcUserService = jcUserService;
        this.jcfMessageService = jcfMessageService;
        this.jcfReadStatusService = jcfReadStatusService;
        this.jfcUserStatusService = jfcUserStatusService;
        this.channelList.putAll(loadChannelText());
    }

    @PreDestroy
    public void cleanup() {
        saveChannelText();
    }

    @Override
    public void createNewChannel(String name) {
        super.createNewChannel(name);
        //System.out.println(name);
        saveChannelText();
    }

    @Override
    public Map<UUID, Channel> loadChannelText(){
        Map<UUID, Channel> loadTxt=new TreeMap<>();
        File file = new File(fileName);


        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            boolean dirsCreated = parentDir.mkdirs();
            if (!dirsCreated) {
                System.err.println("디렉터리를 생성할 수 없습니다.");
                return loadTxt;
            }
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("파일을 생성할 수 없습니다: " + e.getMessage());
                return loadTxt;
            }
        }

        if (file.length() == 0) {
            return loadTxt;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line.trim());
            }

            JsonNode rootNode = objectMapper.readTree(content.toString());

            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String key = field.getKey();
                JsonNode channelData = field.getValue();

                UUID id = UUID.fromString(key);

                Instant createdAt = Instant.ofEpochMilli(channelData.get("createdAt").asLong());
                Instant updatedAt = channelData.has("updatedAt")
                        ? Instant.ofEpochMilli(channelData.get("updatedAt").asLong())
                        : null;
                String channelName = channelData.get("channelName").asText();
                ChannelType channelType = ChannelType.valueOf(channelData.get("channelType").asText().toUpperCase());

                Map<User, UserStatus> userList = new TreeMap<>();
                JsonNode userListNode = channelData.get("userList");
                if (userListNode != null) {
                    Iterator<Map.Entry<String, JsonNode>> userEntries = userListNode.fields();
                    while (userEntries.hasNext()) {
                        Map.Entry<String, JsonNode> userEntry = userEntries.next();
                        UUID userId = UUID.fromString(userEntry.getKey());
                        UUID userStatusId = UUID.fromString(userEntry.getValue().asText());
                        User user = jcUserService.readUser(userId).get(0);
                        UserStatus userStatus = jfcUserStatusService.findById(userStatusId);
                        if (user != null && userStatus != null) {
                            userList.put(user, userStatus);
                        }
                    }
                }
//
                Map<User, ReadStatus> readStatusList = new TreeMap<>();
                JsonNode readStatusNode = channelData.get("readStatus");
                if (readStatusNode != null && !readStatusNode.isNull()) {
                    Iterator<Map.Entry<String, JsonNode>> readStatusEntries = readStatusNode.fields();
                    while (readStatusEntries.hasNext()) {
                        Map.Entry<String, JsonNode> readStatusEntry = readStatusEntries.next();
                        UUID userId = UUID.fromString(readStatusEntry.getKey());
                        UUID readStatusId = UUID.fromString(readStatusEntry.getValue().asText());
                        User user = jcUserService.readUser(userId).get(0);
                        ReadStatus readStatus = jcfReadStatusService.findById(readStatusId);
                        if (user != null && readStatus != null) {
                            readStatusList.put(user, readStatus);
                        }
                    }
                }

                List<Message> messageList=new ArrayList<>();
                JsonNode messageListNode=channelData.get("messageList");
                if(messageListNode!=null){
                    for (JsonNode messageNode : messageListNode){
                        List<Message> messages = jcfMessageService.readMessage(messageNode);
                        if (!messages.isEmpty()) {  
                            Message message = messages.get(0);
                            messageList.add(message);
                        }
                    }
                }


                Channel channel=Channel.createChannelAll(id,createdAt,updatedAt,channelName,channelType
                ,userList,readStatusList,messageList);
                existChannelIdCheck.add(id);
                existNameCheck.add(channelName);
                loadTxt.put(id, channel);

            }
        } catch (IOException e) {
            System.err.println("파일을 불러올수 수 없습니다: " + e.getMessage());
        }
        return loadTxt;
    }

    @Override
    public void saveChannelText(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {

            Map<UUID, Map<String, Object>> saveData = new TreeMap<>();

            for (Map.Entry<UUID, Channel> entry : this.channelList.entrySet()) {
                Channel channel = entry.getValue();

                Map<String, Object> channelData = new TreeMap<>();
                channelData.put("channelName", channel.getChannelName());
                channelData.put("createdAt", channel.getCreatedAt());
                channelData.put("updatedAt", channel.getUpdatedAt());
                channelData.put("channelType", channel.getChannelType().name());


                Map<String, Object> userListData =new HashMap<>();
                for (Map.Entry<User, UserStatus> userEntry : channel.getUserList().entrySet()) {
                    userListData.put(userEntry.getKey().getId().toString(),userEntry.getValue().getId().toString());
                }
                channelData.put("userList", userListData);

                Map<String, Object> readListData =new HashMap<>();
                if(channel.getReadStatusList()!=null) {
                    for (Map.Entry<User, ReadStatus> userEntry : channel.getReadStatusList().entrySet()) {
                        readListData.put(userEntry.getKey().getId().toString(),userEntry.getValue().getId().toString());
                    }
                    channelData.put("readStatus", readListData);
                }else{
                    channelData.put("readStatus", "");
                }


                channelData.put("messageList",channel.getMessageIdList());

                saveData.put(entry.getKey(), channelData);
            }

            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            String jsonString = writer.writeValueAsString(saveData);
            bw.write(jsonString);

        } catch (IOException e) {
            System.err.println("파일을 불러올수 수 없습니다: " + e.getMessage());
        }
    }

    @Override
    protected boolean deleteChannelInfo(LinkedHashMap<UUID,Channel> instance) {
        if(super.deleteChannelInfo(instance)){
            saveChannelText();
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public <T, K> ReadStatus  addUserToChannel(T channelName, K userName){
        super.addUserToChannel(channelName,userName);
        saveChannelText();
        return null;
    }


    @Override
    protected boolean updateChannelInfo(LinkedHashMap<UUID,Channel> instance, String changeName) {
        if(super.updateChannelInfo(instance,changeName)){
            saveChannelText();
            return true;
        }
        else{
            return false;
        }

    }


    @Override
    public void deleteUserToChannel(String channelName, String userName){
        super.deleteUserToChannel(channelName,userName);
        saveChannelText();
    }

    @Override
    public void createPrivateChannel(){
        super.createPrivateChannel();
        saveChannelText();
    }
    @Override
    public <T,K,C,Q> boolean sendMessageInUser(T channel, K sender, C reciver,Q message){
        boolean result=super.sendMessageInUser(channel,sender,reciver,message);
        saveChannelText();
        return result;
    }


}
