package service;


import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.dto.binary_content.CreateBinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.basic.BinaryContentServiceImpl;
import com.sprint.mission.discodeit.util.FileType;
import com.sprint.mission.discodeit.validator.EntityValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BinaryContentServiceImplTest {

  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private EntityValidator validator;
  @InjectMocks
  private BinaryContentServiceImpl binaryContentService;

  private BinaryContent content1;
  private BinaryContent content2;
  private CreateBinaryContentDto createBinaryContentDto;
  private BinaryContentDto binaryContentDto;
  private User user1;
  @BeforeEach
  void setUp(){

    user1 = new User.UserBuilder("username", "password", "email@gmail.com", "01012341234").nickname("nickname").build();

    binaryContentDto = new BinaryContentDto("fileName", FileType.JPG, 3, new byte[]{1,2,3});
    createBinaryContentDto = new CreateBinaryContentDto(
        user1.getUUID(),
        null,
        "fileName",
        FileType.JPG,
        3,
        new byte[]{1,2,3}
    );

    content1 = new BinaryContent.BinaryContentBuilder(user1.getUUID(), binaryContentDto.fileName(), binaryContentDto.fileType(), binaryContentDto.fileSize(), binaryContentDto.data()).build();
    content2 = new BinaryContent.BinaryContentBuilder(
        user1.getUUID(),
        "fileName2",
        FileType.JPG,
        3,
        new byte[]{3,4,5}
    ).build();
  }

  @Test
  void testCreate_Success(){
    when(validator.findOrThrow(eq(User.class), eq(user1.getUUID()), any(UserNotFoundException.class))).thenReturn(user1);
    when(binaryContentRepository.save(any(BinaryContent.class))).thenAnswer(content -> content.getArgument(0));

    BinaryContent result = binaryContentService.create(createBinaryContentDto);

    assertThat(result).isNotNull();
    assertThat(result.getUserId()).isEqualTo(user1.getUUID());
    assertThat(result.getData()).hasSize(3);

    verify(validator, times(1)).findOrThrow(eq(User.class), eq(user1.getUUID()), any(UserNotFoundException.class));
    verify(binaryContentRepository, times(1)).save(any(BinaryContent.class));
  }

  @Test
  void testCreate_Fail_UserNotFound(){
    when(validator.findOrThrow(eq(User.class), eq("invalid-user-id"), any(UserNotFoundException.class)))
        .thenThrow(new UserNotFoundException());

    assertThatThrownBy(() -> binaryContentService.create(
        new CreateBinaryContentDto("invalid-user-id", null, "fileName", FileType.JPG, 3, new byte[]{1, 2, 3})
    )).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void testFind(){
    when(binaryContentRepository.findById(content1.getUUID())).thenReturn(Optional.ofNullable(content1));

    BinaryContent content = binaryContentService.find(content1.getUUID());

    assertThat(content).isNotNull();
    assertThat(content.getUserId()).isEqualTo(user1.getUUID());
    assertThat(content.getFileName()).isEqualTo("fileName");

    verify(binaryContentRepository, times(1)).findById(content1.getUUID());
  }

  @Test
  void testFind_Fail(){
    when(binaryContentRepository.findById("invalid-id")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> binaryContentService.find("invalid-id")).isInstanceOf(InvalidOperationException.class).hasMessageContaining(DEFAULT_ERROR_MESSAGE);
  }

  @Test
  void testFindAllByIdIn_Success(){
    when(binaryContentRepository.findById(content1.getUUID())).thenReturn(Optional.ofNullable(content1));
    when(binaryContentRepository.findById(content2.getUUID())).thenReturn(Optional.ofNullable(content2));

    List<BinaryContent> contents = binaryContentService.findAllByIdIn(List.of(content1.getUUID(), content2.getUUID()));

    assertThat(contents).isNotNull();
    assertThat(contents).hasSize(2);
    assertThat(contents.get(0).getFileName()).isEqualTo("fileName");

    verify(binaryContentRepository, times(2)).findById(anyString()) ;
  }


  @Test
  void testFindAllByIdIn_Fail(){
    when(binaryContentRepository.findById(content1.getUUID())).thenReturn(Optional.empty());

    assertThatThrownBy(() -> binaryContentService.findAllByIdIn(List.of(content1.getUUID()))).isInstanceOf(InvalidOperationException.class).hasMessageContaining(DEFAULT_ERROR_MESSAGE);

  }

  @Test
  void testDelete(){
    binaryContentService.delete(content1.getUUID());

    verify(binaryContentRepository, times(1)).deleteById(content1.getUUID());
  }
}


