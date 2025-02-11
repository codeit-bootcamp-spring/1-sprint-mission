package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.jcf.JCFBinaryContentService;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

@Service("FileBinaryContentService")
public class FileBinaryContentService extends JCFBinaryContentService implements BinaryContentRepository,Serializable {

    public FileBinaryContentService(){
        binaryContentRepository= loadBinaryContentText();
    }

    @PreDestroy
    public void cleanup() {
        saveBinaryContentText();
    }
    private static final String fileName = Paths.get("src", "main", "repo", "binaryContent.txt").toString();
    @Override
    public BinaryContent create(char[] img) {
        BinaryContent binaryContent=super.create(img);
        saveBinaryContentText();
        return  binaryContent;
    }
    @Override
    public void delete(UUID id) {
        super.delete(id);
        saveBinaryContentText();
    }
    @Override
    public Map<UUID, BinaryContent> loadBinaryContentText() {
        Map<UUID,BinaryContent> loadTxt=new HashMap<>();

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
                UUID id = UUID.fromString(field.getKey());
                JsonNode node = field.getValue();

                Instant createdAt = Instant.parse(node.get("createdAt").asText());
                char[] img = node.get("img").asText().toCharArray();

                BinaryContent binaryContent = BinaryContent.makeAllBinaryContent (id, createdAt, img);
                loadTxt.put(id, binaryContent);
            }
        } catch (IOException e) {
            System.err.println("파일을 불러올수 수 없습니다: " + e.getMessage());
        }
        return loadTxt;
    }


    @Override
    public void saveBinaryContentText() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {

            Map<UUID, Map<String, Object>> saveData = new TreeMap<>();

            for (Map.Entry<UUID, BinaryContent> entry : binaryContentRepository.entrySet()) {
                BinaryContent binaryContent = entry.getValue();
                Map<String, Object> data = new TreeMap<>();
                data.put("id", binaryContent.getId());
                data.put("createdAt", binaryContent.getCreatedAt());
                data.put("img", binaryContent.getImg() != null ? binaryContent.getImg() : "");
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
