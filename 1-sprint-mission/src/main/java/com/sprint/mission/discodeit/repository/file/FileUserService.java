package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.sprint.mission.discodeit.entity.User.createUserAll;

public class FileUserService extends JCFUserService implements Serializable, UserRepository {
    private static final long serialVersionUID = 1L;
    private static final String fileName =  ".\\1-sprint-mission\\1-sprint-mission\\src\\main\\repo\\user.json";


    public FileUserService(){

        userList=loadUserTxt();
    }

    @Override
    public void createNewUser(String name) {
        super.createNewUser(name);
        saveUserTxt();
    }

    @Override
    public Map<UUID, User> loadUserTxt() {
        Map<UUID, User> loadTxt = new TreeMap<>();
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
                JsonNode userData = field.getValue();

                String name = userData.get("name").asText();
                long createdAt = userData.get("createdAt").asLong();
                long updatedAt = userData.has("updatedAt") ? userData.get("updatedAt").asLong() : 0;

                UUID id = UUID.fromString(key);
                User user =  createUserAll(id, createdAt, updatedAt, name);


                loadTxt.put(id, user);
                existUserIdCheck.add(id);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadTxt;
    }



    @Override
    public void saveUserTxt() {
        ObjectMapper objectMapper = new ObjectMapper();
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream( fileName), StandardCharsets.UTF_8))) {

            Map<UUID, Map<String, Object>> saveData = new TreeMap<>();

            for (Map.Entry<UUID, User> entry : userList.entrySet()) {
                User user = entry.getValue();
                Map<String, Object> userData = new TreeMap<>();
                userData.put("name", user.getName());
                userData.put("createdAt", user.getCreatedAt());
                userData.put("updatedAt", user.getUpdatedAt());

                saveData.put(entry.getKey(), userData);
            }

            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            String jsonString = writer.writeValueAsString(saveData);
            bw.write(jsonString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean deleteUserInfo(LinkedHashMap<UUID,User> instance){
        if(super.deleteUserInfo(instance)){
            saveUserTxt();
            return true;
        }
        else{
            return false;
        }
    }
    @Override
    protected boolean updateUserNameInfo(LinkedHashMap<UUID,User> instance, String changeName) {
        if(super.updateUserNameInfo(instance,changeName)){
            saveUserTxt();
            return true;
        }
        else{
            return false;
        }

    }
}
