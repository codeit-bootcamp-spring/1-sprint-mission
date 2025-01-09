package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.ChannelEntity;
import com.sprint.mission.discodeit.entity.MessageEntity;
import com.sprint.mission.discodeit.entity.UserEntity;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private final List<MessageEntity> data = new ArrayList<>();

    @Override
    public void addMessage(MessageEntity message) {
        data.add(message);
    }

    @Override
    public void updateMessage(UUID id, UUID userId, UUID channelId, String content) {
        MessageEntity message = getMessageById(id);
        if (message != null){
            message.updateContent(content);
            message.updateChannelId(channelId);
            message.updateUpdatedAt(System.currentTimeMillis());
            System.out.println("메시지가 수정되었습니다.");
        } else {
            System.out.println("메시지 수정 중에 오류가 발생했습니다. 다시 시도해주세요.");
        }
    }

    @Override
    public void updateMessageContent(UUID id, String content) {
        MessageEntity message = getMessageById(id);
        if (message != null){
            message.updateContent(content);
            message.updateUpdatedAt(System.currentTimeMillis());
            System.out.println("메시지가 수정되었습니다.");
        } else {
            System.out.println("메시지 수정 중에 오류가 발생했습니다. 다시 시도해주세요.");
        }
    }

    @Override
    public void updateMessageChannel(UUID id, UUID channelId) {
        MessageEntity message = getMessageById(id);
        if (message != null){
            message.updateChannelId(channelId);
            message.updateUpdatedAt(System.currentTimeMillis());
            System.out.println("메시지가 수정되었습니다.");
        } else {
            System.out.println("메시지 수정 중에 오류가 발생했습니다. 다시 시도해주세요.");
        }
    }

    @Override
    public void updateMessageChannel(UUID id, UUID channelId, String content) {

    }

    @Override
    public void updateMessageUser(UUID id, UUID userId) {

    }

    @Override
    public void deleteMessage(UUID id) {
        if(getMessageById(id) != null){
            data.removeIf(message -> message.getId().equals(id));
            System.out.println("메시지가 삭제되었습니다.");
        } else {
            System.out.println("메시지 삭제 중에 오류가 발생하였습니다. 다시 시도해주세요.");
        }
    }

    @Override
    public void deleteMessageWithFlg(UUID id) {
        MessageEntity message = getMessageById(id);
        if(message != null){
            message.updateMessageDelFlg("1");
        }
    }

    @Override
    public MessageEntity getMessageById(UUID id) {
        return data.stream().filter(message -> message.getId().equals(id))
                .findFirst().orElse(null);
    }

    @Override
    public void deleteMessageByChannelId(UUID channelId) {
        data.removeIf(message -> message.getChannelId().equals(channelId));
    }

    @Override
    public void deleteMessageByChannelIdWithFlg(UUID channelId) {
        List<MessageEntity> list = getMessageByChannelIdWithFlg(channelId);
        if (list.size() != 0){
            for(MessageEntity message : list){
                message.updateMessageDelFlg("1");
            }
            System.out.println("채널의 메시지가 모두 삭제되었습니다. (" + list.size() + " 건)");
        } else {
            System.out.println("채널에 삭제할 메시지가 없습니다.");
        }
    }

    @Override
    public List<MessageEntity> getMessageByChannelId(UUID channelId) {
        return data.stream().filter(message -> message.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public MessageEntity getMessageByIdWithFlg(UUID id) {
        return data.stream().filter(message -> message.getId().equals(id) && message.getMessageDelFlg().equals("0"))
                .findFirst().orElse(null);
    }

    @Override
    public List<MessageEntity> getMessageByChannelIdWithFlg(UUID channelId) {
        return data.stream().filter(message -> message.getChannelId().equals(channelId) && message.getMessageDelFlg().equals("0"))
                .collect(Collectors.toList());
    }

}
