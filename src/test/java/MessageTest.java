//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//public class MessageTest {
//
//    private final JCFMessageService instance = JCFMessageService.getInstance();
//
//    @DisplayName("메시지를 저장할수 있다")
//    @Test
//    void 메시지를_저장할수_있다(){
//        Message message = new Message(
//            "user-uuid-1",
//            "channel-uuid-1",
//            "this is example content",
//            "example-image.com",
//            null
//        );
//
//        instance.createMessage(message);
//
//        Assertions.assertThat(message).isEqualTo(
//            instance.getMessageById(message.getUUID()).get()
//        );
//    }
//
//    @DisplayName("메시지를 수정할 수 있다")
//    @Test
//    void 메시지를_수정할_수_있다(){
//        Message message = new Message(
//            "user-uuid-1",
//            "channel-uuid-1",
//            "this is example content",
//            "example-image.com",
//            null
//        );
//
//        instance.createMessage(message);
//        String messageId = message.getUUID();
//
//        instance.updateMessage(messageId, "this is updated content", null);
//
//        Assertions.assertThat(message).isEqualTo(
//            instance.getMessageById(messageId).get()
//        );
//
//        Assertions.assertThat(message.getContent()).isEqualTo("this is updated content");
//        Assertions.assertThat(message.getIsEdited()).isTrue();
//    }
//
//    @DisplayName("메시지를 삭제할 수 있다")
//    @Test
//    void 메시지를_삭제할수_있다(){
//        Message message = new Message(
//            "user-uuid-1",
//            "channel-uuid-1",
//            "this is example content",
//            "example-image.com",
//            null
//        );
//
//        instance.createMessage(message);
//        String messageId = message.getUUID();
//
//        instance.deleteMessage(messageId);
//
//        Assertions.assertThat(instance.getMessageById(messageId)).isEmpty();
//    }
//}
