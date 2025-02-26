//package com.sprint.mission.discodeit.repository.file.json;
//
//import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
//import com.sprint.mission.discodeit.entity.message.Message;
//import com.sprint.mission.discodeit.entity.user.User;
//import com.sprint.mission.discodeit.repository.BinaryContentRepository;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.basic.BasicMessageService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.Map;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class JsonBinaryContentRepositoryTest {
//
//    ChannelRepository channelRepository = new JsonChannelRepository();
//    BinaryContentRepository binaryContentRepository = new JsonBinaryContentRepository();
//    MessageRepository messageRepository = new JsonMessageRepository();
//    MessageService messageService = new BasicMessageService(messageRepository, binaryContentRepository, channelRepository);
//
//
//    User user1 = new User("userA", "test@gmail.com", "password");
//    User user2 = new User("userB", "22test@gmail.com", "pas222sword");
//    Message message = new Message("title", "hello", user1, user2);
//    Message message2 = new Message("hello", "me", user2, user1);
//    Message message3 = new Message("파일 하나 더 보냄", "ㅇㅇ", user1, user2);
//    BinaryContent binaryContent1 = new BinaryContent(user1.getId(), message.getId(), "fileName", "json");
//    BinaryContent binaryContent3 = new BinaryContent(user1.getId(), message3.getId(), "두번째로보낸파일", "java");
//    BinaryContent binaryContent2 = new BinaryContent(user2.getId(), message2.getId(), "fileName", "png");
//
//    @Test
//    @DisplayName("BinaryContent 생성")
//    void create() {
//        binaryContentRepository.save(binaryContent1);
//        binaryContentRepository.save(binaryContent2);
//
//        Map<UUID, BinaryContent> findMap = binaryContentRepository.findByUserId(user1.getId());
//        BinaryContent binaryContent = findMap.get(binaryContent1.getId());
//        assertThat(binaryContent).isEqualTo(binaryContent1);
//    }
//
//    @Test
//    @DisplayName("message에 있는 첨부 파일 찾기")
//    void findFileByMessage() {
//        binaryContentRepository.save(binaryContent1);
//
//        BinaryContent byMesasgeId = binaryContentRepository.findByMessageId(message.getId());
//
//        assertThat(byMesasgeId).isEqualTo(binaryContent1);
//    }
//
//    @Test
//    @DisplayName("모든 파일 찾기")
//    void findAll() {
//        binaryContentRepository.save(binaryContent1);
//        binaryContentRepository.save(binaryContent3);
//
//        Map<UUID, BinaryContent> all = binaryContentRepository.findAll();
//
//        assertThat(all.size()).isEqualTo(2);
//    }
//
//    /**
//     * @Description: 유저가 보낸 모든 파일 삭제
//     */
//    @Test
//    @DisplayName("유저가 보낸 모든 파일 삭제")
//    void deleteUserAllFile() {
//        binaryContentRepository.save(binaryContent1);
//        binaryContentRepository.save(binaryContent3);
//        Map<UUID, BinaryContent> findAll = binaryContentRepository.findAll();
//
//
//        binaryContentRepository.removeAllContentOfUser(user1.getId());
//
//
//        assertThat(findAll.size()).isEqualTo(0);
//    }
//
//
//    @Test
//    @DisplayName("유저가 보낸 메세지의 파일 하나 삭제")
//    void remove() {
//        binaryContentRepository.save(binaryContent1);
//        binaryContentRepository.save(binaryContent3);
//        Map<UUID, BinaryContent> findAll = binaryContentRepository.findAll();
//
//        binaryContentRepository.removeContent(message.getId());
//
//        assertThat(findAll.size()).isEqualTo(1);
//    }
//}