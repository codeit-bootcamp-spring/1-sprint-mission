package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.DTO.MessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("JCFMessageService")
public class JCFMessageService implements MessageService {
    protected Map<UUID, Message> messageList=new LinkedHashMap<>();
    protected static final Set<UUID> existMessageIdCheck=new HashSet<>();
    @Autowired
    @Qualifier("FileBinaryContentService")
    private JCFBinaryContentService jcfBinaryContentService;


    @Override
    public void createNewMessage(String title, String body) {
        Message newMessage;
        do{
            newMessage=Message.createDefaultMessage(title,body);
        }while(existMessageIdCheck.contains(newMessage.getId()));
        messageList.put(newMessage.getId(),newMessage);
        existMessageIdCheck.add(newMessage.getId());


    }



    @Override
    public void createNewMessage(String title, String body, List<BinaryContent> binaryContents) {
        Message newMessage;
        do{
            newMessage=Message.createDefaultMessage(title,body,binaryContents);
        }while(existMessageIdCheck.contains(newMessage.getId()));
        messageList.put(newMessage.getId(),newMessage);
        existMessageIdCheck.add(newMessage.getId());


    }

    @Override
    public void createNewMessagetoImg(String title, String body, List<char[]> imgs) {
        List<BinaryContent> contents=new ArrayList<>();
        for(char[] img:imgs){
            contents.add(jcfBinaryContentService.create(img));
        }
        createNewMessage(title,body,contents);
    }

    @Override
    public <T>List<Message> readMessage(T key) {
        List<Message> list= new ArrayList<>();

        LinkedHashMap<UUID,Message> foundMessage=new LinkedHashMap<>();

        if (key instanceof String) {
            foundMessage = findMessage((String) key);
        } else if (key instanceof UUID) {
            foundMessage = findMessage((UUID) key);
        }

        for (Message val : foundMessage.values()) {
            list.add(val);
        }

        return list;
    }


    @Override
    public List<Message> readMessageAll() {
        List<Message> list= new ArrayList<>();
        for (Message val : messageList.values()) {
            list.add(val);
        }
        if(!list.toString().isEmpty())
            return list;
        else
            return null;
    }

    @Override
    public boolean updateMessageTitle(UUID ID, String change) {
        LinkedHashMap<UUID,Message> instance = findMessage(ID);
        return changeMessageTitle(instance,change);
    }

    @Override
    public boolean updateMessageTitle(String title, String change) {
        LinkedHashMap<UUID,Message> instance = findMessage(title);
        return changeMessageTitle(instance,change);
    }
    @Override
    public boolean updateMessageBody(UUID ID, String change) {
        LinkedHashMap<UUID,Message> instance = findMessage(ID);
        return changeMessageBody(instance,change);
    }

    @Override
    public boolean updateMessageBody(String title, String change) {
        LinkedHashMap<UUID,Message> instance = findMessage(title);
        return changeMessageBody(instance,change);
    }

    @Override
    public boolean deleteMessage(UUID id) {
        LinkedHashMap<UUID, Message> instance = findMessage(id);
        return deleteMessageInfo(instance);
    }

    @Override
    public boolean deleteMessage(String title) {
        LinkedHashMap<UUID, Message> instance = findMessage(title);
        return deleteMessageInfo(instance);
    }

    @Override
    public boolean updateMessageTitle(MessageDto messageDto) {
        LinkedHashMap<UUID, Message> instance = findMessage(messageDto.id());
        return changeMessageTitle(instance, messageDto.title());
    }

    @Override
    public boolean updateMessageBody(MessageDto  messageDto) {
        LinkedHashMap<UUID, Message> instance = findMessage(messageDto.id());
        return changeMessageBody(instance, messageDto.messageBody());
    }




    protected LinkedHashMap<UUID,Message> findMessage(UUID id) {
        LinkedHashMap<UUID, Message> findMessage = new LinkedHashMap<>();
        findMessage.put(id,messageList.get(id));
        return findMessage;
    }


    protected LinkedHashMap<UUID,Message> findMessage(String title) {
        LinkedHashMap<UUID, Message> findMessage = new LinkedHashMap<>();
        for(Message message:messageList.values()){
            if(message.getTitle().equals(title)){
                findMessage.put(message.getId(),message);
            }
        }
        return findMessage;
    }

    protected boolean deleteMessageInfo(LinkedHashMap<UUID, Message> instance) {
        if (instance.isEmpty()) {
            System.out.println( "해당하는 메세지가가 없습니다.");
            return false;
        } else if (instance.size() == 1) {
            Message find = instance.entrySet().iterator().next().getValue();
            existMessageIdCheck.remove(find.getId());
            messageList.remove(find.getId());
            System.out.println("성공적으로 삭제했습니다.");
            return true;
        } else {
            System.out.println("중복된 메세지가 있습니다.");
            return false;
        }
    }

    protected boolean changeMessageTitle(LinkedHashMap<UUID, Message> instance, String changetitle) {
        if (instance==null||instance.isEmpty()) {
            System.out.println( "해당하는 메세지가가 없습니다.");
            return false;
        } else if (instance.size() == 1) {
            Message firstMessage = instance.entrySet().iterator().next().getValue();
            firstMessage.setTitle(changetitle);
            firstMessage.setUpdatedAt();
            System.out.println("성공적으로 바꿨습니다.");
            return true;
        }
        return false;
    }

    protected boolean changeMessageBody(LinkedHashMap<UUID, Message> instance, String changeBody) {
        if (instance==null||instance.isEmpty())  {
            System.out.println( "해당하는 메세지가가 없습니다.");
            return false;
        } else if (instance.size() == 1) {
            Message firstMessage = instance.entrySet().iterator().next().getValue();
            firstMessage.setMessageBody(changeBody);
            firstMessage.setUpdatedAt();
            System.out.println("성공적으로 바꿨습니다.");
            return true;
        }else{
            return false;
        }

    }

    @Override
    public void addMessage(Message m) {
        messageList.put(m.getId(),m);
    }


}
