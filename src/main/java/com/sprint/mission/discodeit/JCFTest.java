//package com.sprint.mission.discodeit;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
//
//public class JCFTest {
//    public static void main(String[] args) {
//        JCFUserRepository jcfUserRepository = new JCFUserRepository();
//        JCFChannelRepository jcfChannelRepository = new JCFChannelRepository();
//        JCFMessageRepository jcfMessageRepository = new JCFMessageRepository();
//        System.out.println("\n JCF repository test =======");
//
//        //유저 테스트
//        System.out.println("\n 유저테스트 =======");
//        User user1 = new User("Alice", "password123");
//        User user2 = new User("Belle", "password456");
//
//        jcfUserRepository.save(user1);
//        jcfUserRepository.save(user2);
//
//        System.out.println("유저 전체 조회: \n");
//        for (User user : jcfUserRepository.findAll()){
//            System.out.println(user.getUsername() + " ID : " + user.getId() );
//        }
//
//        //채널테스트
//        System.out.println("\n 채널 테스트 ======");
//        Channel channel1 = new Channel("channel1");
//        Channel channel2 = new Channel("channel2");
//
//        jcfChannelRepository.save(channel1);
//        jcfChannelRepository.save(channel2);
//
//        System.out.println("채널 전체 조회: \n");
//        for (Channel channel : jcfChannelRepository.findAll()){
//            System.out.println(channel.getChannelName() + " ID : " + channel.getId() );
//        }
//
//
//        //메세지 테스트
//        System.out.println("\n 메세지 테스트 ======");
//        Message message1 = new Message(user1.getUsername(), channel1.getId(), "채널 1 테스트!");
//        Message message2 = new Message(user2.getUsername(), channel1.getId(), "체널 1 2사용자 테스트!");
//
//        jcfMessageRepository.save(message1);
//        jcfMessageRepository.save(message2);
//
//        System.out.println("메세지 전체 조회: \n");
//        for (Message message : jcfMessageRepository.findAll()){
//            System.out.println(message.getUsername() + " : " + message.getChannelId()+" : " + message.getContent());
//        }
//
//        //삭제 테스트
//        System.out.println("\n 삭제 테스트 ======");
//
//        jcfMessageRepository.deleteById(message1.getId());
//        jcfUserRepository.deleteById(message1.getId());
//        jcfChannelRepository.deleteById(channel1.getId());
//
//        System.out.println("\n삭제 후 남은 데이터 테스트 ======");
//        System.out.println("유저 전체 조회: ");
//        for (User user : jcfUserRepository.findAll()){
//            System.out.println(user.getUsername() + " ID : " + user.getId() );
//        }
//        System.out.println("채널 전체 조회: \n");
//        for (Channel channel : jcfChannelRepository.findAll()){
//            System.out.println(channel.getChannelName() + " ID : " + channel.getId() );
//        }
//        System.out.println("메세지 전체 조회: \n");
//        for (Message message : jcfMessageRepository.findAll()){
//            System.out.println(message.getUsername() + " : " + message.getChannelId()+" : " + message.getContent());
//        }
//    }
//}
