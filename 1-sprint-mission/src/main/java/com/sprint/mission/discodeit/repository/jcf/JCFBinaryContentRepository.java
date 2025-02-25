package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class JCFBinaryContentRepository {
    List<BinaryContent> contents = new ArrayList<>();

    public void saveUserContent(UUID id){
        BinaryContent binaryContent = new BinaryContent();
        binaryContent.setUserId(id);
        binaryContent.setCreatedAt(Instant.now());
        binaryContent.setMessageId(null);

        contents.add(binaryContent);
    }

    public void saveMessageContent(UUID id){
        BinaryContent binaryContent = new BinaryContent();
        binaryContent.setMessageId(id);
        binaryContent.setCreatedAt(Instant.now());
        binaryContent.setUserId(null);

        contents.add(binaryContent);
    }

    public void deleteUser(UUID id) {
        contents.removeIf(content -> content.getUserId().equals(id));
    }

    public void deleteMessage(UUID id){
        contents.removeIf(content -> content.getMessageId().equals(id));
    }

    public BinaryContent findMessageContent(UUID messageId) {
        for(BinaryContent content : contents){
            if(content.getMessageId().equals(messageId)){
                return content;
            }
        }

        return null;
    }


    public BinaryContent findUserContent(UUID userId){
        for(BinaryContent content : contents){
            if(content.getUserId().equals(userId)){
                return content;
            }
        }

        return null;
    }

    public List<BinaryContent> findAll(){
        return contents;
    }
}
