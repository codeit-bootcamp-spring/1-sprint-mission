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
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class FileChannelService extends JCFChannelService implements ChannelService, ChannelRepository {

    private static final String fileName =  ".\\1-sprint-mission\\1-sprint-mission\\src\\main\\repo\\channel.txt";

    public FileChannelService(){
        channelList=LoadChannelTxt();
    }

    @Override
    public void CreateChannelDefault(String name) {
        channelList=LoadChannelTxt();
        if(find_Channel(name).isEmpty()){
            Channel new_channel=Channel.CreateDefaultChannel(name);
            channelList.put(new_channel.getId(),new_channel);
        }else {
            System.out.println("이미 존재하는 채널입니다.");
        }
        SaveChannelTxt();
    }

    @Override
    public Map<UUID, Channel> LoadChannelTxt(){
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

                Channel message = new Channel(id,createdAt,updatedAt,channelName);
                loadTxt.put(id, message);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadTxt;
    }

    @Override
    public void SaveChannelTxt(){
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
    protected boolean DeleteChannel(TreeMap<UUID,Channel> instance) {
        if (instance==null||instance.isEmpty()) {
            System.out.println("해당하는 채널이 없습니다.");
            return false;
        }else if(instance.size()==1){
            Channel find=instance.firstEntry().getValue();
            find.deleteExistChannelId();
            channelList.remove(find.getId());
            SaveChannelTxt();
            System.out.println("성공적으로 삭제했습니다.");
            return true;
        }else{
            System.out.println("중복된 채널이 있습니다.");
            return false;
        }
    }

    @Override
    protected boolean UpdateChannel(TreeMap<UUID,Channel> instance, String changeName) {
        if (instance==null||instance.isEmpty())  {
            System.out.println( "해당하는 채널이 없습니다.");
            return false;
        } else if (instance.size() == 1) {
            Channel find = instance.firstEntry().getValue();
            find.setChannelName(changeName);
            find.setUpdatedAt();
            SaveChannelTxt();
            System.out.println("성공적으로 바꿨습니다.");
            return true;
        }else{
            System.out.println("중복된 채널이 있습니다.");
            return false;
        }

    }
}
