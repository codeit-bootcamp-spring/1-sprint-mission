//package com.sprint.mission.discodeit.service.basic;
//
//import static org.junit.Assert.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//
//import com.sprint.mission.discodeit.dto.request.MessageRequest;
//import com.sprint.mission.discodeit.dto.response.MessageResponse;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.BinaryContentRepository;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.storage.BinaryContentStorage;
//import java.util.UUID;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.annotation.Transactional;
//
//@ActiveProfiles("test")
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@Transactional
//@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//public class BasicMessageServiceTest {
//
//    @Autowired
//    MessageService messageService;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    ChannelRepository channelRepository;
//
//    @Autowired
//    MessageRepository messageRepository;
//
//    @Autowired
//    BinaryContentRepository binaryContentRepository;
//
//    @Autowired
//    BinaryContentStorage binaryContentStorage;
//
//    private UUID messageId;
//    private UUID authorId;
//
//    @BeforeEach
//    void setup() {
//        // 테스트용 유저와 메시지 생성
//        User author = userRepository.findByUsername("testUser")
//            .orElseThrow(() -> new RuntimeException("유저 없음"));
//        Message message = messageRepository.findAll().get(0);
//
//        messageId = message.getId();
//        authorId = author.getId();
//    }
//
//    @Test
//    @WithUserDetails(value = "testUser", userDetailsServiceBeanName = "customUserDetailService")
//    void update_권한있을때_성공() {
//        MessageRequest.Update update = new MessageRequest.Update("new content");
//
//        // 👉 이 유저가 메시지 작성자라면 성공
//        MessageResponse response = messageService.update(messageId, update);
//
//        assertEquals("new content", response.content());
//    }
//
//    @Test
//    @WithMockUser(username = "notAuthor", roles = {"USER"})
//    void update_권한없을때_예외() {
//        MessageRequest.Update update = new MessageRequest.Update("new content");
//
//        assertThrows(AccessDeniedException.class, () -> {
//            messageService.update(messageId, update);
//        });
//    }
//
//    @Test
//    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
//    void delete_admin이면_삭제가능() {
//        messageService.deleteById(messageId);
//        assertFalse(messageRepository.findById(messageId).isPresent());
//    }
//
//}
