package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.jcf.JFCUserStatusService;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;


@Service("FileUserStatusService")
public class FileUserStatusService extends JFCUserStatusService implements UserStatusRepository,Serializable {

    private static final String fileName = Paths.get("src", "main", "repo", "userStatus.txt").toString();

    public FileUserStatusService(){
        this.userStatusMap= loadUserStatusText();
    }

    @PreDestroy
    public void cleanup() {
        saveUserStatusText();
    }


    @Override
    public boolean create(UUID id){

        if(!super.create(id))
            return false;
        saveUserStatusText();
        return true;
    }

    @Override
    public boolean update(UUID id) {
        if(super.update(id)==false)
            return false;
        saveUserStatusText();
        return true;
    }

    @Override
    public boolean updateByUserId(UUID id) {
        if(super.updateByUserId(id)==false)
            return false;
        saveUserStatusText();
        return true;
    }

    @Override
    public void delete(List<UserStatus> list){
        super.delete(list);
        saveUserStatusText();
    }

    @Override
    public void add(UserStatus userStatus){
        super.add(userStatus);
        saveUserStatusText();
    }

    @Override
    public Map<UUID, UserStatus> loadUserStatusText(){
        Map<UUID, UserStatus> loadTxt = new TreeMap<>();
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
        objectMapper.registerModule(new JavaTimeModule());

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
                Map.Entry<String, JsonNode> entry = fields.next();
                UUID id = UUID.fromString(entry.getKey());
                JsonNode node = entry.getValue();

                UUID userId = UUID.fromString(node.get("userId").asText());
                Instant createdAt = Instant.ofEpochMilli(node.get("createdAt").asLong());
                Instant updatedAt = node.has("updatedAt") && !node.get("updatedAt").isNull()
                        ? Instant.ofEpochMilli(node.get("updatedAt").asLong())
                        : null;

                UserStatus userStatus = UserStatus.makeAllUserStatus(id, createdAt, updatedAt,userId);
                loadTxt.put(id, userStatus);
                userRepository.add(userId);
            }

        } catch (IOException e) {
            System.err.println("파일에 문제가 있음");
        }

        return loadTxt;
    }

    @Override
    public void saveUserStatusText(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {

            Map<UUID, Map<String, Object>> saveData = new TreeMap<>();

            for (Map.Entry<UUID, UserStatus> entry : userStatusMap.entrySet()) {
                UserStatus userStatus = entry.getValue();
                Map<String, Object> Data = new TreeMap<>();
                Data.put("createdAt", userStatus.getCreatedAt().toEpochMilli());
                Data.put("updatedAt", userStatus.getUpdatedAt() != null ? userStatus.getUpdatedAt().toEpochMilli() : null);
                Data.put("userId", userStatus.getUserId());


                saveData.put(entry.getKey(), Data);
            }

            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            String jsonString = writer.writeValueAsString(saveData);
            bw.write(jsonString);

        } catch (IOException e) {
            System.err.println("파일에 문제가 있음");
        }
    }







}
