package com.sprint.mission.discodeit.service.jcf;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import java.util.Scanner;


public class JCFMessageService implements MessageService {
    Scanner sc = new Scanner(System.in);
    private final ArrayList<Message> messages;
    public JCFMessageService(){
        messages = new ArrayList<>();
    }
    public void Create(Channel channel, String messageText){
        messages.add(new Message(channel, messageText));
    }

    // Read : 전체 메세지 조회, 특정 메세지 읽기
    public void ReadAll(){
        for(Message message: messages){
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^message^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            System.out.println("User : " + message.getChannel().getUser().getNickname());
            System.out.println("Channel : " + message.getChannel().getChannelName());
            System.out.println(message.getMessageText());
            System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
        }
    }
    public void ReadMessage(int num){
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^message^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println("User : " + messages.get(num).getChannel().getUser().getNickname());
        System.out.println("Channel : " + messages.get(num).getChannel().getChannelName());
        // 개발자가 핸들링 한다고 생각해서,
        // 만약 사용자가 핸들링 한다면,
        // "User이(내가) 보낸 메세지 보기", "특정 채널에 보냈던 메세지 보기"같은 기능을 상정해서
        // User나 Channel을 통해 볼 수 있는 게 맞는 것 같다.
        System.out.println(messages.get(num).getMessageText());
        System.out.println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
    }

    // Update : 특정 메세지 수정
    public void Update(int num){
        String messageText = sc.next();
        messages.get(num).setMessageText(messageText);
        messages.get(num).setUpdateAt(System.currentTimeMillis());
    }

    // Delete : 전체 메세지 삭제, 특정 메세지 삭제
    public void DeleteAll(){
        System.out.println("Do you really delete everything?\n");
        System.out.println("                 [y/n]");
        String keyword = sc.next().toLowerCase();
        if(keyword.equals("y")){
            messages.clear();
        }
    }
    public void DeleteMessage(int num){
        System.out.println("Do you really delete Message?\n");
        System.out.println("                 [y/n]");
        String keyword = sc.next().toLowerCase();
        if(keyword.equals("y")){
            messages.remove(num);
        }
    }
}
