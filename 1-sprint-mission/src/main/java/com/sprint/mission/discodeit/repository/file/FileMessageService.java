package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

@Repository
@Service("FileMessageService")
public class FileMessageService extends JCFMessageService implements MessageService, MessageRepository {

    private static final String fileName = Paths.get("1-sprint-mission","1-sprint-mission","src", "main", "repo", "message.txt").toString();
    @Autowired
    @Qualifier("JCFBinaryContentService")
    private final BinaryContentService JCFBinaryContentService;

    public FileMessageService(@Qualifier("FileBinaryContentService") BinaryContentService jcfBinaryContentService){
        JCFBinaryContentService = jcfBinaryContentService;
        messageList= loadMessageText();
    }

    @PreDestroy
    public void cleanup() {
        saveMessageText();
    }

    @Override
    public void createNewMessage(String title, String body) {
        super.createNewMessage(title,body);
        saveMessageText();
    }
    @Override
    public void createNewMessage(String title, String body, List<BinaryContent> binaryContents) {
        super.createNewMessage(title,body,binaryContents);
        saveMessageText();
    }

    @Override
    public void createNewMessagetoImg(String title, String body, List<char[]> imgs){
        super.createNewMessagetoImg(title,body,imgs);
        saveMessageText();

    }


    @Override
    public void addMessage(Message m) {
        super.addMessage(m);
        saveMessageText();
    }

    @Override
    protected boolean deleteMessageInfo(LinkedHashMap<UUID, Message> instance) {
        if(super.deleteMessageInfo(instance)){
            saveMessageText();
            return true;
        }else{
            return false;
        }
    }
    @Override
    protected boolean changeMessageTitle(LinkedHashMap<UUID, Message> instance, String changetitle) {
        if(super.changeMessageTitle(instance,changetitle)){
            saveMessageText();
            return true;
        }else{
            return false;
        }
    }
    @Override
    protected boolean changeMessageBody(LinkedHashMap<UUID, Message> instance, String changeBody) {
        if(super.changeMessageBody(instance,changeBody)){
            saveMessageText();
            return true;
        }else {
            return false;
        }
    }


    @Override
    public Map<UUID,Message> loadMessageText(){
        Map<UUID,Message> loadTxt=new TreeMap<>();
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
                JsonNode messageData = field.getValue();

                UUID id = UUID.fromString(key);

                Instant createdAt = Instant.ofEpochMilli(messageData.get("createdAt").asLong());
                Instant updatedAt = messageData.has("updatedAt")
                        ? Instant.ofEpochMilli(messageData.get("updatedAt").asLong())
                        : null;
                String title = messageData.get("title").asText();
                String messageBody = messageData.get("messageBody").asText();
                String senderName = messageData.get("senderName").asText();
                String receiverName = messageData.get("receiverName").asText();

                UUID senderId = null;
                if (messageData.has("senderId") && !messageData.get("senderId").isNull()) {
                    senderId = UUID.fromString(messageData.get("senderId").asText());
                }

                UUID receiverId = null;
                if (messageData.has("receiverId") && !messageData.get("receiverId").isNull()) {
                    receiverId = UUID.fromString(messageData.get("receiverId").asText());
                }


                JsonNode binaryContentsNode = (JsonNode) messageData.get("binaryContents");
                List<UUID> binaryContentIds = objectMapper.convertValue(binaryContentsNode, new TypeReference<List<UUID>>() {});

                ArrayList<BinaryContent> binaryContents = new ArrayList<>(JCFBinaryContentService.findAllByIdIn(binaryContentIds));

                Message message = Message.createChannelAll(id, createdAt, updatedAt, title, messageBody,
                        binaryContents, senderName, receiverName, senderId, receiverId);
                existMessageIdCheck.add(message.getId());


                loadTxt.put(id, message);

            }
        } catch (IOException e) {
            System.err.println("파일을 불러올수 수 없습니다: " + e.getMessage());
        }
        return loadTxt;
    }
    @Override
    public void saveMessageText() {






        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {


            Map<UUID, Map<String, Object>> saveData = new TreeMap<>();


            for (Map.Entry<UUID, Message> entry : messageList.entrySet()) {
                Message message = entry.getValue();


                Map<String, Object> messageData = new TreeMap<>();
                messageData.put("title", message.getTitle());
                messageData.put("createdAt", message.getCreatedAt());
                messageData.put("updatedAt", message.getUpdatedAt());
                messageData.put("messageBody", message.getMessageBody());
                messageData.put("senderName", message.getSenderName());
                messageData.put("receiverName", message.getReceiverName());
                messageData.put("senderId", message.getSenderId() != null ? message.getSenderId() : null);
                messageData.put("receiverId", message.getReceiverId() != null ? message.getReceiverId() : null);

                messageData.put("binaryContents", message.getBinaryConetentIdList());

                saveData.put(entry.getKey(),messageData);
            }



            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            String jsonString = writer.writeValueAsString(saveData);
            bw.write(jsonString);

        } catch (IOException e) {
            System.err.println("파일을 불러올수 수 없습니다: " + e.getMessage());
        }
    }

}
