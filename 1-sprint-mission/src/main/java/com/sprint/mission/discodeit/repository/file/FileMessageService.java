package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileMessageService extends JCFMessageService implements MessageService, MessageRepository {

    private static final String fileName = ".\\1-sprint-mission\\1-sprint-mission\\src\\main\\repo\\message.json";

    public FileMessageService(){
        messageList=loadMessageTxt();
    }

    @Override
    public void createNewMessage(String title, String body) {
        super.createNewMessage(title,body);
        saveMessageTxt();
    }


    @Override
    public void addMessage(Message m) {
        super.addMessage(m);
        saveMessageTxt();
    }

    @Override
    protected boolean deleteMessageInfo(LinkedHashMap<UUID, Message> instance) {
        if(super.deleteMessageInfo(instance)){
            saveMessageTxt();
            return true;
        }else{
            return false;
        }
    }
    @Override
    protected boolean changeMessageTitle(LinkedHashMap<UUID, Message> instance, String changetitle) {
        if(super.changeMessageTitle(instance,changetitle)){
            saveMessageTxt();
            return true;
        }else{
            return false;
        }
    }
    @Override
    protected boolean changeMessageBody(LinkedHashMap<UUID, Message> instance, String changeBody) {
        if(super.changeMessageBody(instance,changeBody)){
            saveMessageTxt();
            return true;
        }else {
            return false;
        }
    }
    @Override
    public Map<UUID,Message> loadMessageTxt(){
        Map<UUID,Message> loadTxt=new TreeMap<>();
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
                String key = field.getKey();
                JsonNode messageData = field.getValue();

                UUID id = UUID.fromString(key);

                long createdAt = messageData.get("createdAt").asLong();
                long updatedAt = messageData.has("updatedAt") ? messageData.get("updatedAt").asLong() : 0;
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

                Message message = Message.createChannelAll(id, createdAt, updatedAt, title,
                        messageBody,senderName,receiverName,senderId,receiverId);
                existMessageIdCheck.add(message.getId());


                loadTxt.put(id, message);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadTxt;
    }
    @Override
    public void saveMessageTxt() {
        ObjectMapper objectMapper = new ObjectMapper();
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
                messageData.put("senderId", message.getSenderID() != null ? message.getSenderID() : null);
                messageData.put("receiverId", message.getReceiverID() != null ? message.getReceiverID() : null);


                saveData.put(entry.getKey(), messageData);
            }

            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            String jsonString = writer.writeValueAsString(saveData);
            bw.write(jsonString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
