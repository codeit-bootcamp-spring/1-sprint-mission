package com.sprint.mission.discodeit.repository.file;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FileChannelService extends JCFChannelService implements ChannelService, ChannelRepository {

    private static final String fileName =  ".\\1-sprint-mission\\1-sprint-mission\\src\\main\\repo\\channel.json";

    public FileChannelService(){
        channelList=loadChannelTxt();
    }

    @Override
    public void createNewChannel(String name) {
        super.createNewChannel(name);
        saveChannelTxt();
    }

    @Override
    public Map<UUID, Channel> loadChannelTxt(){
        Map<UUID, Channel> loadTxt=new TreeMap<>();
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
                JsonNode channelData = field.getValue();

                UUID id = UUID.fromString(key);

                long createdAt = channelData.get("createdAt").asLong();
                long updatedAt = channelData.has("updatedAt") ? channelData.get("updatedAt").asLong() : 0;
                String channelName = channelData.get("channelName").asText();

                Channel message = Channel.createChannelAll(id,createdAt,updatedAt,channelName);
                existChannelIdCheck.add(id);
                loadTxt.put(id, message);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadTxt;
    }

    @Override
    public void saveChannelTxt(){
        ObjectMapper objectMapper = new ObjectMapper();
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {

            Map<UUID, Map<String, Object>> saveData = new TreeMap<>();

            for (Map.Entry<UUID, Channel> entry : channelList.entrySet()) {
                Channel channel = entry.getValue();

                Map<String, Object> channelData = new TreeMap<>();
                channelData.put("channelName", channel.getChannelName());
                channelData.put("createdAt", channel.getCreatedAt());
                channelData.put("updatedAt", channel.getUpdatedAt());


                saveData.put(entry.getKey(), channelData);
            }

            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
            String jsonString = writer.writeValueAsString(saveData);
            bw.write(jsonString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean deleteChannelInfo(LinkedHashMap<UUID,Channel> instance) {
        if(super.deleteChannelInfo(instance)){
            saveChannelTxt();
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    protected boolean updateChannelInfo(LinkedHashMap<UUID,Channel> instance, String changeName) {
        if(super.updateChannelInfo(instance,changeName)){
            saveChannelTxt();
            return true;
        }
        else{
            return false;
        }

    }
}
