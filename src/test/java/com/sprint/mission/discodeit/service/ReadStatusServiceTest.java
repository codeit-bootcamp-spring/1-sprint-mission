package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.request.CreateReadStatusRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.execption.message.MessageNotFoundException;
import com.sprint.mission.discodeit.global.error.execption.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileReadStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileStorage;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicReadStatusService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import com.sprint.mission.discodeit.validator.ReadStatusValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReadStatusServiceTest {

    private ReadStatusRepository readStatusRepository;
    private UserRepository userRepository;
    private ChannelRepository channelRepository;
    private ReadStatusValidator readStatusValidator;
    private UserValidator userValidator;
    private ChannelValidator channelValidator;
    private ReadStatusService readStatusService;
    private FileStorage fileStorage;

    @BeforeEach
    void setUp() {
//        jcfSetUp();
        fileSetUp();
        readStatusValidator = new ReadStatusValidator(readStatusRepository);
        userValidator = new UserValidator(userRepository);
        channelValidator = new ChannelValidator(channelRepository);
        readStatusService = new BasicReadStatusService(readStatusRepository, readStatusValidator,
                userValidator, channelValidator);
    }

    @AfterEach
    void clean() {
        if (fileStorage != null) {
            fileStorage.clearDataDirectory();
        }
    }

    private void jcfSetUp() {
        readStatusRepository = new JCFReadStatusRepository();
        channelRepository = new JCFChannelRepository();
        userRepository = new JCFUserRepository();
    }

    private void fileSetUp() {
        fileStorage = new FileStorage();
        readStatusRepository = new FileReadStatusRepository(fileStorage);
        channelRepository = new FileChannelRepository(fileStorage);
        userRepository = new FileUserRepository(fileStorage);
    }

    private User createUser(int num) {
        User user = User.of("test" + num, "nickname" + num, "email" + num, "password" + num);
        return userRepository.saveUser(user);
    }

    private Channel createPublicChannel(User channelOwner, int num) {
        String channelName = "channel" + num;
        String channelDescription = "description";
        Channel channel = Channel.of(channelName, channelDescription, channelOwner, ChannelType.PUBLIC);
        return channelRepository.saveChannel(channel);
    }

    private ReadStatus createReadStatus(User user, Channel channel) {
        ReadStatus readStatus = ReadStatus.of(user, channel);
        return readStatusRepository.saveReadStatus(readStatus);
    }

    @Nested
    @DisplayName("ReadStatus 생성 테스트")
    class CreateReadStatusTest {

        @Test
        @DisplayName("ReadStatus 생성 성공")
        void success() {
            // given
            User user = createUser(0);
            Channel channel = createPublicChannel(user, 0);

            CreateReadStatusRequest createReadStatusRequest =
                    new CreateReadStatusRequest(user.getId(), channel.getId());

            // when
            ReadStatus readStatus = readStatusService.createReadStatus(createReadStatusRequest);

            // then
            assertNotNull(readStatusRepository.findReadStatusById(readStatus.getId()));
        }
    }

    @Nested
    @DisplayName("ReadStatus 단일 조회 테스트")
    class FindReadStatusTest {

        @Test
        @DisplayName("단일 조회 성공 by readStatusId")
        void successByReadStatusId() {
            // given
            User user = createUser(0);
            Channel channel = createPublicChannel(user, 0);
            ReadStatus readStatus = createReadStatus(user, channel);

            // when
            ReadStatus foundedReadStatus = readStatusService.findReadStatusById(readStatus.getId());

            // then
            assertEquals(readStatus, foundedReadStatus);
        }

        @Test
        @DisplayName("존재하지 않는 Id 조회 후 예외 발생")
        void findReadStatusById_ThrowsException_WhenIdDoesNotExist() {
            // given
            User user = createUser(0);
            Channel channel = createPublicChannel(user, 0);
            createReadStatus(user, channel);

            UUID randomId = UUID.randomUUID();

            // when & then
            assertThatThrownBy(() -> readStatusService.findReadStatusById(randomId))
                    .isInstanceOf(ReadStatusNotFoundException.class)
                    .hasMessage(ErrorCode.READSTATUS_NOT_FOUND.format("id: " + randomId));
        }

        @Test
        @DisplayName("단일 조회 성공 by userId")
        void successByUserId() {
            // given
            User user = createUser(0);
            Channel channel = createPublicChannel(user, 0);
            ReadStatus readStatus = createReadStatus(user, channel);

            // when
            ReadStatus foundedReadStatus = readStatusService.findAllReadStatusesByUserId(user.getId());

            // then
            assertEquals(readStatus, foundedReadStatus);
        }

        @Test
        @DisplayName("존재하지 않는 userId 조회 후 예외 발생")
        void findReadStatusById_ThrowsException_WhenUserIdDoesNotExist() {
            // given
            User user = createUser(0);
            Channel channel = createPublicChannel(user, 0);
            createReadStatus(user, channel);

            UUID randomId = UUID.randomUUID();

            // when & then
            assertThatThrownBy(() -> readStatusService.findAllReadStatusesByUserId(randomId))
                    .isInstanceOf(ReadStatusNotFoundException.class)
                    .hasMessage(ErrorCode.READSTATUS_NOT_FOUND.format("userId: " + randomId));
        }
    }

    @Nested
    @DisplayName("ReadStatus 수정 테스트")
    class UpdateReadStatusTest {

        @Test
        @DisplayName("ReadStatus 수정 성공")
        void success() {
            // given
            User user = createUser(0);
            Channel channel = createPublicChannel(user, 0);
            ReadStatus readStatus = createReadStatus(user, channel);

            // when
            ReadStatus updatedReadStatus = readStatusService.updateReadStatus(readStatus.getId());

            // then
            assertNotEquals(readStatus.getUpdatedAt(), updatedReadStatus);
        }
    }

    @Nested
    @DisplayName("ReadStatus 삭제 테스트")
    class DeleteReadStatusTest {

        @Test
        @DisplayName("ReadStatus 삭제 성공")
        void success() {
            // given
            User user = createUser(0);
            Channel channel = createPublicChannel(user, 0);
            ReadStatus readStatus = createReadStatus(user, channel);

            // when
            readStatusService.deleteReadStatus(readStatus.getId());

            // then
            assertNull(readStatusRepository.findReadStatusById(readStatus.getId()));
        }
    }

}