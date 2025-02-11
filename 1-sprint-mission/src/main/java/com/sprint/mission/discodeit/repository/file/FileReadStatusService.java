package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.DTO.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.DTO.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.jcf.JCFReadStatusService;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

@Service("FileReadStatusService")
public class FileReadStatusService extends JCFReadStatusService implements ReadStatusRepository,Serializable {

    private static final long serialVersionUID = 1L;
    private static final String fileName = Paths.get("src", "main", "repo", "readStatus.txt").toString();
    public FileReadStatusService(){
        readStatusMap= loadReadStatusText();
    }

    @PreDestroy
    public void cleanup() {
        saveReadStatusText();
    }

    @Override
    public ReadStatus create(ReadStatusCreateRequest request){
        ReadStatus readStatus=super.create(request);
        saveReadStatusText();
        return  readStatus;
    }

    @Override
    public boolean update(ReadStatusUpdateRequest request) {
        if(!super.update(request))
            return false;
        saveReadStatusText();
        return true;
    }

    @Override
    public void delete(List<ReadStatus> list) {
        super.delete(list);
        saveReadStatusText();
    }

    @Override
    public void deleteUser(UUID userId) {
        super.deleteUser(userId);
    }
    @Override
    public void addReadStatus(ReadStatus readStatus){
        super.addReadStatus(readStatus);
        saveReadStatusText();
    }



        @Override
    public Map<UUID, ReadStatus> loadReadStatusText() {
            Map<UUID, ReadStatus> loadTxt = new TreeMap<>();
            File file = new File(fileName);


            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    System.err.println("파일을 생성할 수 없습니다: " + e.getMessage());
                }
                return loadTxt;
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
                    JsonNode data = field.getValue();

                    UUID id = UUID.fromString(data.get("userID").toString());
                    UUID userId = UUID.fromString(data.get("userID").toString());
                    UUID channelId = UUID.fromString(data.get("channelId").toString());
                    Instant createdAt = Instant.ofEpochMilli(data.get("createdAt").asLong());
                    Instant updatedAt = data.has("updatedAt")
                            ? Instant.ofEpochMilli(data.get("updatedAt").asLong())
                            : null;
                    boolean isRead;
                    if(data.get("isRead").asInt()==1)
                        isRead=true;
                    else{
                        isRead=false;
                    }

                    ReadStatus readStatus=ReadStatus.makeAllReadStatus(id,createdAt,updatedAt,userId,channelId,isRead);
                    readStatusMap.put(readStatus.getId(),readStatus);
                }

            } catch (IOException e) {
                System.err.println("파일을 불러올수 수 없습니다: " + e.getMessage());
            }
            return loadTxt;
    }

    @Override
    public void saveReadStatusText() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {

            Map<UUID, Map<String, Object>> saveData = new TreeMap<>();

            for (Map.Entry<UUID, ReadStatus> entry : readStatusMap.entrySet()) {
                ReadStatus readStatus = entry.getValue();
                Map<String, Object> data = new TreeMap<>();
                
                data.put("id", readStatus.getId().toString());
                data.put("userID", readStatus.getUserId().toString());
                data.put("channelId", readStatus.getChannelId().toString());
                data.put("createdAt", readStatus.getCreatedAt().toEpochMilli());
                data.put("updatedAt", readStatus.getUpdatedAt() != null ? readStatus.getUpdatedAt().toEpochMilli() : null);


                data.put("isRead", readStatus.isRead() ? 1 : 0);

                saveData.put(entry.getKey(), data);
            }

            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            String jsonString = writer.writeValueAsString(saveData);
            bw.write(jsonString);

        } catch (IOException e) {
            System.err.println("파일을 불러올수 수 없습니다: " + e.getMessage());
        }
    }
}
