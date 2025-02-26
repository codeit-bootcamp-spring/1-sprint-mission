package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

import static com.sprint.mission.discodeit.entity.User.createUserAll;

@Service("FileUserService")
public class FileUserService extends JCFUserService implements Serializable, UserRepository {
    private static final long serialVersionUID = 1L;

    private static final String fileName = Paths.get("1-sprint-mission","1-sprint-mission","src", "main", "repo", "user.txt").toString();
    private final BinaryContentService JCFBinaryContentService;
    private final UserStatusService JFCUserStatusService;
    private final JCFMessageService jcfMessageService;


    public FileUserService(@Qualifier("FileBinaryContentService") BinaryContentService jcfBinaryContentService,
                           @Qualifier("FileUserStatusService") UserStatusService jfcUserStatusService, @Qualifier("FileMessageService") JCFMessageService jcfMessageService){
        JCFBinaryContentService = jcfBinaryContentService;
        JFCUserStatusService = jfcUserStatusService;
        this.jcfMessageService = jcfMessageService;
        //super.userList.clear();
        this.userList.putAll(loadUserText());
    }

    @PreDestroy
    public void cleanup() {
        saveUserText();
    }

    @Override
    public void createNewUser(String name,String password,String email) {
        super.createNewUser(name,password,email);
        saveUserText();
    }

    @Override
    public Map<UUID, User> loadUserText() {
        Map<UUID, User> loadTxt = new TreeMap<>();
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
                JsonNode userData = field.getValue();

                String name = userData.get("name").asText();
                String email=userData.get("email").asText();
                JsonNode passwordNode = userData.get("password");
                long password = (passwordNode != null && passwordNode.isNumber()) ? passwordNode.asLong() : 0;
                Instant createdAt = Instant.ofEpochMilli(userData.get("createdAt").asLong());
                Instant updatedAt = userData.has("updatedAt")
                        ? Instant.ofEpochMilli(userData.get("updatedAt").asLong())
                        : null;

                JsonNode selfImgNode = userData.get("selfImg");
                BinaryContent selfImg = null;
                if (selfImgNode != null) {
                    String selfImgStr = selfImgNode.asText();
                    if (selfImgStr != null && !selfImgStr.trim().isEmpty()) {
                        try {
                            UUID selfImgId = UUID.fromString(selfImgStr);
                            selfImg = JCFBinaryContentService.findById(selfImgId);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid userStatus UUID: " + selfImgStr);
                        }
                    }
                }
                JsonNode userStatusNode = userData.get("userStatus");
                UserStatus userStatus = null;
                if (userStatusNode != null) {
                    String userStatusStr = userStatusNode.asText();
                    if (userStatusStr != null && !userStatusStr.trim().isEmpty()) {
                        try {
                            UUID userStatusId = UUID.fromString(userStatusStr);
                            userStatus = JFCUserStatusService.findById(userStatusId);
                        } catch (IllegalArgumentException e) {

                            System.err.println("Invalid userStatus UUID: " + userStatusStr);
                        }
                    }
                }

                List<Message> messageList = new ArrayList<>();
                JsonNode userMessage = userData.get("userMessage");

                if (userMessage != null && !userMessage.isEmpty()) {
                    for (JsonNode messageNode : userMessage) {
                        List<Message> messages = jcfMessageService.readMessage(messageNode);

                        if (messages != null && !messages.isEmpty()) {
                            messageList.add(messages.get(0));
                        } else {
                           break;
                        }
                    }
                }


                UUID id = UUID.fromString(key);
                User user =  createUserAll(id,  createdAt,  updatedAt,  name, password,  email,selfImg,userStatus,messageList);


                loadTxt.put(id, user);
                existUserIdCheck.add(id);
                existUserEmailCheck.add(email);
                existUserNameCheck.add(name);

            }
        } catch (IOException e) {
            System.err.println("파일에 문제가 있음");
        }
        return loadTxt;
    }



    @Override
    public void saveUserText() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {

            Map<UUID, Map<String, Object>> saveData = new TreeMap<>();

            for (Map.Entry<UUID, User> entry : this.userList.entrySet()) {
                User user = entry.getValue();
                Map<String, Object> userData = new TreeMap<>();
                userData.put("name", user.getName());
                userData.put("email", user.getEmail());
                userData.put("password", user.getPassword());
                userData.put("createdAt", user.getCreatedAt().toEpochMilli());
                userData.put("updatedAt", user.getUpdatedAt() != null ? user.getUpdatedAt().toEpochMilli() : null);
                userData.put("selfImg",user.getSelfImg()!= null ?user.getSelfImg().getId():"");
                userData.put("userStatus",user.getUserStatus()!= null? user.getUserStatus().getId():"");
                userData.put("userMessage",user.getUserMessageId());


                saveData.put(entry.getKey(), userData);
            }

            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            String jsonString = writer.writeValueAsString(saveData);
            bw.write(jsonString);

        } catch (IOException e) {
            System.err.println("파일에 문제가 있음");
        }
    }


    @Override
    protected boolean deleteUserInfo(LinkedHashMap<UUID,User> instance,String password){
        if(super.deleteUserInfo(instance,password)){
            saveUserText();
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    protected boolean updateUserNameInfo(LinkedHashMap<UUID,User> instance, String changeName) {
        if(super.updateUserNameInfo(instance,changeName)){
            saveUserText();
            return true;
        }
        else{
            return false;
        }

    }
    @Override
    protected boolean updateSelfImg(LinkedHashMap<UUID,User> instance,char [] img){
        if(super.updateSelfImg(instance,img)){
            saveUserText();
            return true;
        }
        else{
            return false;
        }
    }


}
