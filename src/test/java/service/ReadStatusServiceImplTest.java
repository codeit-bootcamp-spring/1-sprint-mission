package service;

import com.sprint.mission.discodeit.dto.ReadStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.basic.ReadStatusServiceImpl;
import com.sprint.mission.discodeit.validator.EntityValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReadStatusServiceImplTest {

  @Mock
  private ReadStatusRepository readStatusRepository;
  @Mock
  private EntityValidator validator;
  @InjectMocks
  ReadStatusServiceImpl readStatusService;
  private ReadStatus status1, status2;


  @BeforeEach
  void setUp(){
    status1 = new ReadStatus("channel1", "user1");
    status2 = new ReadStatus("channel2", "user1");
  }

  @Test
  void testCreate_Success(){
    CreateReadStatusDto dto = new CreateReadStatusDto(status1.getChannelId(), status1.getUserId());

//    when(validator.findOrThrow(eq(User.class), eq("user1"), any(UserNotFoundException.class))).thenReturn();
//    when(validator.findOrThrow(eq(Channel.class), eq("channel1"), any(ChannelNotFoundException.class))).thenReturn((Channel) new Object());

    when(readStatusRepository.findByUserId("user1")).thenReturn(List.of());
    when(readStatusRepository.save(any(ReadStatus.class))).thenAnswer(status -> status.getArgument(0));

    ReadStatus status = readStatusService.create(dto, false);

    assertThat(status).isNotNull();
    assertThat(status.getUserId()).isEqualTo("user1");
    assertThat(status.getChannelId()).isEqualTo("channel1");

    verify(validator, times(1)).findOrThrow(eq(User.class), eq("user1"), any(UserNotFoundException.class));
    verify(validator, times(1)).findOrThrow(eq(Channel.class), eq("channel1"), any(ChannelNotFoundException.class));
    verify(readStatusRepository, times(1)).findByUserId("user1");
  }

  @Test
  void testCreate_Fail(){
    CreateReadStatusDto dto = new CreateReadStatusDto(status1.getChannelId(), status1.getUserId());
    when(validator.findOrThrow(eq(User.class), eq("user1"), any(UserNotFoundException.class))).thenThrow(new UserNotFoundException());

    assertThatThrownBy(() -> readStatusService.create(dto, false)).isInstanceOf(UserNotFoundException.class);
    verifyNoMoreInteractions(validator);
    verifyNoInteractions(readStatusRepository);
  }

  @Test
  void testCreate_Fail_Duplicate(){
    CreateReadStatusDto dto = new CreateReadStatusDto(status1.getChannelId(), status1.getUserId());
    when(readStatusRepository.findByUserId("user1")).thenReturn(List.of(status1));

    assertThatThrownBy(() -> readStatusService.create(dto, false)).isInstanceOf(InvalidOperationException.class);
  }

  @Test
  void testFindSuccess(){
    when(readStatusRepository.findById(status1.getUUID())).thenReturn(Optional.ofNullable(status1));

    ReadStatus res = readStatusService.find(status1.getUUID());

    assertThat(res).isNotNull();
    assertThat(res.getChannelId()).isEqualTo("channel1");
    assertThat(res.getUserId()).isEqualTo("user1");
  }

  @Test
  void testUpdateSuccess(){
    CreateReadStatusDto dto = new CreateReadStatusDto(status1.getChannelId(), status1.getUserId());
    when(readStatusRepository.findByChannelIdAndUserId(dto.channelId(), dto.userId())).thenReturn(Optional.ofNullable(status1));

    ReadStatus result = readStatusService.update(dto);

    assertThat(result).isNotNull();
    assertThat(result.getUserId()).isEqualTo(dto.userId());
    assertThat(result.getChannelId()).isEqualTo(dto.channelId());
    verify(readStatusRepository, times(1)).save(any(ReadStatus.class));

  }

  @Test
  void testUpdate_NotFound() {
    CreateReadStatusDto dto = new CreateReadStatusDto(status1.getChannelId(), status1.getUserId());

    when(readStatusRepository.findByChannelIdAndUserId("channel1", "user1"))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> readStatusService.update(dto))
        .isInstanceOf(InvalidOperationException.class);

    verify(readStatusRepository, never()).save(any(ReadStatus.class));
  }


}
