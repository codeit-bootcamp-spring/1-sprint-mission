package com.sprint.mission.unit.service;

//create, update, delete 메소드
//핵심 메소드에 대해 각각 최소 2개 이상(성공, 실패)의 테스트 케이스를 작성


import com.sprint.mission.dto.BinaryContentMapper;
import com.sprint.mission.dto.MessageMapper;
import com.sprint.mission.dto.request.BinaryContentDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForCreate;
import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.Message;
import com.sprint.mission.entity.User;
import com.sprint.mission.repository.binary.BinaryContentStorage;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.service.BinaryService;
import com.sprint.mission.service.jcf.serviceImpl.MessageServiceImpl;
import com.sprint.mission.unit.util.MockFileFactory;
import com.sprint.mission.unit.util.ReflectionFieldSetter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.entity.ChannelType.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    private final ReflectionFieldSetter reflectionFieldSetter = new ReflectionFieldSetter();
    private final MockFileFactory mockFileFactory = new MockFileFactory();

    @Spy
    private MessageMapper messageMapper = Mappers.getMapper(MessageMapper.class);
    @Spy
    private BinaryContentMapper binaryContentMapper = Mappers.getMapper(BinaryContentMapper.class);
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private BinaryService binaryService;
    @Mock
    private BinaryContentStorage binaryContentStorage;
    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Test
    @DisplayName("메시지 생성 성공")
    void createSuccess() throws Exception {
        // given
        //    public Message create(MessageDtoForCreate responseDto, List<BinaryContentDtoForCreate> binaryContentDtoForCreateList) {
        Channel channel = (Channel) reflectionFieldSetter.settingFieldValue(new Channel("작성된 곳", "테스트용 채널입니다", PUBLIC));
        User author = (User) reflectionFieldSetter.settingFieldValue(new User("작성자", "패스워드123", "icb1555@naver.com", null));
        MessageDtoForCreate dto = new MessageDtoForCreate(channel.getId(), author.getId(), "성공 할 메시지");
        int numberOfFiles = 3;
        List<MockMultipartFile> mockFileList = mockFileFactory.getMockFileList(numberOfFiles);
        List<BinaryContentDtoForCreate> binaryDTOList = convertMockFileToBinaryDTO(mockFileList);

        when(userRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(channelRepository.findById(channel.getId())).thenReturn(Optional.of(channel));
        when(binaryService.create(any(BinaryContentDtoForCreate.class))).thenAnswer((invocation) -> {
            BinaryContentDtoForCreate binaryDto = invocation.getArgument(0);
            return reflectionFieldSetter.settingFieldValue(binaryContentMapper.toEntity(binaryDto));
        });
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> reflectionFieldSetter.settingFieldValue(invocation.getArgument(0)));

        // when
        Message message = messageService.create(dto, binaryDTOList);

        // then
        assertThat(message).isNotNull();
        assertThat(message.getContent()).isEqualTo(dto.content());
        assertThat(message.getChannel()).isEqualTo(channel);
        assertThat(message.getAuthor()).isEqualTo(author);
        assertThat(message.getMessageAttachments()).hasSize(numberOfFiles);
    }

    private List<BinaryContentDtoForCreate> convertMockFileToBinaryDTO(List<MockMultipartFile> mockFileList) {
        return mockFileList.stream().map((mockFile) -> {
            try {
                return new BinaryContentDtoForCreate(mockFile.getOriginalFilename(), mockFile.getContentType(), mockFile.getSize(), mockFile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}
