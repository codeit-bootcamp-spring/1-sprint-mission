package com.sprint.mission.discodeit.repository.file;

class FileMessageRepositoryTest {

    /*MessageRepository messageRepository = new FileMessageRepository();

    @BeforeEach
    void init() {
        messageRepository = new FileMessageRepository();
    }

    @Test
    @DisplayName("직렬화를 통한 유저 저장")
    void save() {
        Message m = new Message("title", "contetnt",
                new User("sender", "sender@mail.com", "asdasdaa"),
                new User("receiver", "receiver@mail.com", "clclclclcl"));

        Message saveMessage = messageRepository.saveMessage(m);

        Assertions.assertEquals(m, saveMessage);
    }

    @Test
    @DisplayName("유저 단일 조회")
    void find() {
        Message m = new Message("title", "contetnt",
                new User("sender", "sender@mail.com", "asdasdaa"),
                new User("receiver", "receiver@mail.com", "clclclclcl"));

        messageRepository.saveMessage(m);

        Message findMessage = messageRepository.findMessageById(m.getId());

        Assertions.assertEquals(m, findMessage);
    }

    @Test
    @DisplayName("모든 유저 조회")
    void findAll() {
        Message m1 = new Message("title", "contetnt",
                new User("sender", "sender@mail.com", "asdasdaa"),
                new User("receiver", "receiver@mail.com", "clclclclcl"));

        Message m2 = new Message("title", "contetnt",
                new User("sender", "sender@mail.com", "asdasdaa"),
                new User("receiver", "receiver@mail.com", "clclclclcl"));

        messageRepository.saveMessage(m1);
        messageRepository.saveMessage(m2);

        Map<UUID, Message> allMessage = messageRepository.findAllMessage();

        Message find1 = allMessage.get(m1.getId());
        Message find2 = allMessage.get(m2.getId());

        Assertions.assertEquals(m1, find1);
        Assertions.assertEquals(m2, find2);
    }

    @Test
    @DisplayName("유저 삭제")
    void delete() {
        Message m1 = new Message("title", "contetnt",
                new User("sender", "sender@mail.com", "asdasdaa"),
                new User("receiver", "receiver@mail.com", "clclclclcl"));

        Message m2 = new Message("title", "contetnt",
                new User("sender", "sender@mail.com", "asdasdaa"),
                new User("receiver", "receiver@mail.com", "clclclclcl"));

        messageRepository.saveMessage(m1);
        messageRepository.saveMessage(m2);

        messageRepository.removeMessageById(m1.getId());

        Assertions.assertNull(messageRepository.findMessageById(m1.getId()));

    }*/

}