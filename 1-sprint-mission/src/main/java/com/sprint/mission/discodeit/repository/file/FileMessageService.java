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
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class FileMessageService extends JCFMessageService implements MessageService, MessageRepository {

    private static final String fileName = ".\\1-sprint-mission\\1-sprint-mission\\src\\main\\repo\\message.txt";

    public FileMessageService(){
        messageList=LoadMessageTxt();
    }

    @Override
    public void CreateMessageDefault(String title, String body) {
        messageList=LoadMessageTxt();
        if (find_Message(title).isEmpty()) {
            Message new_Message=Message.CreateDefaultMessage(title,body);
            messageList.put(new_Message.getId(),new_Message);
        } else {
            System.out.println("이미 존재하는 제목입니다.");
        }
        SaveMessageTxt();
    }


    @Override
    public void AddMessage(Message m) {
        messageList.put(m.getId(),m);
        SaveMessageTxt();
    }

    @Override
    protected boolean DeleteMessageInfo(TreeMap<UUID, Message> instance) {
        if (instance.isEmpty()) {
            System.out.println( "해당하는 메세지가가 없습니다.");
            return false;
        } else if (instance.size() == 1) {
            Message find = instance.firstEntry().getValue();
            find.deleteExistMessageId();
            messageList.remove(find.getId());
            SaveMessageTxt();
            System.out.println("성공적으로 삭제했습니다.");
            return true;
        } else {
            System.out.println("중복된 메세지가 있습니다.");
            return false;
        }
    }
    @Override
    protected boolean ChangeMessageTitle(TreeMap<UUID, Message> instance, String changetitle) {
        if (instance==null||instance.isEmpty()) {
            System.out.println( "해당하는 메세지가가 없습니다.");
            return false;
        } else if (instance.size() == 1) {
            Message find = instance.firstEntry().getValue();
            find.setTitle(changetitle);
            find.setUpdatedAt();
            SaveMessageTxt();
            System.out.println("성공적으로 바꿨습니다.");
            return true;
        }
        return false;
    }
    @Override
    protected boolean ChangeMessageBody(TreeMap<UUID, Message> instance, String changeBody) {
        if (instance==null||instance.isEmpty())  {
            System.out.println( "해당하는 메세지가가 없습니다.");
            return false;
        } else if (instance.size() == 1) {
            Message find = instance.firstEntry().getValue();
            find.setMessageBody(changeBody);
            find.setUpdatedAt();
            SaveMessageTxt();
            System.out.println("성공적으로 바꿨습니다.");
            return true;
        }else{
            return false;
        }

    }
    @Override
    public Map<UUID,Message> LoadMessageTxt(){
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

                Message message = new Message(id, createdAt, updatedAt, title,
                        messageBody,senderName,receiverName,senderId,receiverId);


                loadTxt.put(id, message);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadTxt;
    }
    @Override
    public void SaveMessageTxt() {
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
