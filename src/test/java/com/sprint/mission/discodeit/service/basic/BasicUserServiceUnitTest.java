package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.Phone;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binarycontent.EmptyFileUploadException;
import com.sprint.mission.discodeit.exception.user.UserException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserUpdateDataNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserValidationException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.util.validation.UserValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class BasicUserServiceUnitTest {

    @InjectMocks
    private BasicUserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserValidator userValidator;
    @Mock
    private UserMapper userMapper;
    @Mock
    private BinaryContentService binaryContentService;
    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Test
    @DisplayName("프로필 사진을 포함하지 않은 사용자를 성공적으로 생성한다. - Mockito 사용")
    void creatUserWithoutProfileSuccess() throws IOException {
        // given
        UserSignupRequestDto dto = new UserSignupRequestDto(
                "cloudsoda", "구름소다", "cloudsoda@mail.com",
                "!@A445sndk", "010-1111-2222", Phone.RegionCode.KR,
                User.UserType.COMMON, "");

        User userEntity = mock(User.class);
        UUID userId = UUID.randomUUID();
        User savedUser = mock(User.class);
        when(savedUser.getId()).thenReturn(userId);

        UserResponseDto responseDto = new UserResponseDto(savedUser.getId(), savedUser.getNickname(), savedUser.getEmail(), null, true);

        when(userRepository.existsUserByEmail(dto.email())).thenReturn(false);
        when(userRepository.existsUserByUsername(dto.username())).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userEntity.getId()).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(savedUser));
        when(userMapper.toResponseDto(savedUser)).thenReturn(responseDto);

        // when
        UserResponseDto result = userService.create(dto, null);

        // then
        Assertions.assertThat(result).isEqualTo(responseDto);

        verify(userValidator).validateCreate(dto);
        verify(userRepository).save(userEntity);
        verify(userMapper).toResponseDto(savedUser);
    }

    @Test
    @DisplayName("프로필 사진을 포함한 사용자를 성공적으로 생성한다. - BDDMockito 사용")
    void creatUserWithProfileSuccess() throws IOException {
        // given
        UserSignupRequestDto dto = new UserSignupRequestDto(
                "cloudsoda", "구름소다", "cloudsoda@mail.com",
                "!@A445sndk", "010-1111-2222", Phone.RegionCode.KR,
                User.UserType.COMMON, "");

        MockMultipartFile profile = new MockMultipartFile("profile", "profile_test.png", "image/png", new byte[]{1, 2, 3});

        User userEntity = mock(User.class);
        UUID userId = UUID.randomUUID();
        User savedUser = mock(User.class);
        given(savedUser.getId()).willReturn(userId);

        UUID profileId = UUID.randomUUID();
        BinaryContent profileEntity = mock(BinaryContent.class);
        given(profileEntity.getId()).willReturn(profileId);

        BinaryContentResponseDto profileDto = new BinaryContentResponseDto(
                profileId, "test.jpg", (long) profile.getSize(), "image/jpeg", profile.getBytes()
        );
        UserResponseDto responseDto = new UserResponseDto(
                userId, "구름소다", "cloudsoda@mail.com", profileDto, true
        );

        given(userRepository.existsUserByEmail(dto.email())).willReturn(false);
        given(userRepository.existsUserByUsername(dto.username())).willReturn(false);
        given(userMapper.toEntity(dto)).willReturn(userEntity);

        given(binaryContentService.create(profile)).willReturn(profileDto);
        given(binaryContentRepository.findById(profileId)).willReturn(Optional.of(profileEntity));
        willDoNothing().given(userEntity).updateProfile(profileEntity);  // mock한 userEntity에 대해 updateProfile 호출

        given(userRepository.save(userEntity)).willReturn(userEntity);
        given(userEntity.getId()).willReturn(userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(savedUser));
        given(userMapper.toResponseDto(savedUser)).willReturn(responseDto);

        // when
        UserResponseDto result = userService.create(dto, profile);

        // then
        Assertions.assertThat(result).isEqualTo(responseDto);

        then(userValidator).should().validateCreate(dto);
        then(userRepository).should().save(userEntity);
        then(userMapper).should().toResponseDto(savedUser);
    }

    @Test
    @DisplayName("사용자 생성 시 프로필 사진 파일이 사용 불가능한 파일일 때 에러가 제대로 전파되는지 확인한다.")
    void shouldFailWhenBinaryContentServiceThrows() throws IOException {
        // given
        UserSignupRequestDto dto = new UserSignupRequestDto(
                "cloudsoda", "구름소다", "cloudsoda@mail.com",
                "!@A445sndk", "010-1111-2222", Phone.RegionCode.KR,
                User.UserType.COMMON, "");

        MockMultipartFile profile = new MockMultipartFile("profile", "profile_test.png", "image/png", new byte[]{});

        User userEntity = mock(User.class);

        given(userRepository.existsUserByEmail(dto.email())).willReturn(false);
        given(userRepository.existsUserByUsername(dto.username())).willReturn(false);
        given(userMapper.toEntity(dto)).willReturn(userEntity);

        given(binaryContentService.create(profile)).willThrow(EmptyFileUploadException.class);

        // when & then
        Assertions.assertThatThrownBy(() -> userService.create(dto, profile))
                .isInstanceOf(EmptyFileUploadException.class);

        then(binaryContentService).should().create(profile);
    }

    @Test
    @DisplayName("사용자 생성 시 요청 Email이 유효하지 않은 경우 예외를 반환한다.")
    void createUserFailedCauseEmailIsNotValid() throws IOException {
        // given
        UserSignupRequestDto dto = new UserSignupRequestDto(
                "cloudsoda", "구름소다", "mail.com",
                "!@A445sndk", "010-1111-2222", Phone.RegionCode.KR,
                User.UserType.COMMON, "");

        // 유효성 검사 예외
        doThrow(new UserValidationException(ErrorCode.INVALID_EMAIL, dto.email()))
                .when(userValidator).validateCreate(dto);

        // when & then
        Throwable thrown = Assertions.catchThrowable(() -> userService.create(dto, null));

        Assertions.assertThat(thrown)
                .isInstanceOf(UserValidationException.class)
                .satisfies(e -> {
                    UserException ex = (UserValidationException) e;
                    Assertions.assertThat(ex.getDetails().get("field")).isEqualTo("mail.com");
                    Assertions.assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.INVALID_EMAIL);
                });


        verify(userValidator).validateCreate(dto);
        verifyNoInteractions(binaryContentService);  // binaryContentService는 호출되지 않아야 함
    }

    @Test
    @DisplayName("사용자 정보의 일부 내용 및 프로필 수정 요청 시 사용자 정보가 성공적으로 수정된다.")
    void updateUserSuccess() throws IOException {
        // given
        UUID userId = UUID.randomUUID();
        User currentUser = mock(User.class);
        User updatedUser = mock(User.class);
        given(updatedUser.getId()).willReturn(userId);

        UUID newProfileId = UUID.randomUUID();
        BinaryContent newProfile = mock(BinaryContent.class);
        BinaryContent oldProfile = mock(BinaryContent.class);

        UserUpdateDto requestDto = new UserUpdateDto("cloud", "구름",
                null, null, null, null, "저는 구름이에요.");
        MockMultipartFile profile = new MockMultipartFile("profile", "profile_update.png", "image/png", new byte[]{1, 2, 3});

        BinaryContentResponseDto profileDto = new BinaryContentResponseDto(
                newProfileId, "profile_update.png", (long) profile.getSize(), "image/jpeg", profile.getBytes()
        );
        UserResponseDto responseDto = new UserResponseDto(
                userId, "구름", "cloudsoda@mail.com", profileDto, true
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(currentUser));
        given(userValidator.validateUpdate(currentUser, requestDto)).willReturn(updatedUser);
        given(updatedUser.getId()).willReturn(userId);

        given(updatedUser.getProfile()).willReturn(oldProfile);
        willDoNothing().given(binaryContentRepository).delete(oldProfile);

        given(binaryContentService.create(profile)).willReturn(profileDto);
        given(binaryContentRepository.findById(newProfileId)).willReturn(Optional.of(newProfile));

        willDoNothing().given(updatedUser).updateProfile(newProfile);

        given(userRepository.save(updatedUser)).willReturn(updatedUser);
        given(userMapper.toResponseDto(updatedUser)).willReturn(responseDto);

        // when
        UserResponseDto result = userService.update(userId, requestDto, profile);

        // then
        Assertions.assertThat(result).isEqualTo(responseDto);

        verify(userRepository).findById(userId);
        verify(userValidator).validateUpdate(currentUser, requestDto);
        verify(binaryContentRepository).delete(oldProfile);
        verify(binaryContentService).create(profile);
        verify(binaryContentRepository).findById(newProfileId);
        verify(updatedUser).updateProfile(newProfile);
        verify(userRepository).save(updatedUser);
        verify(userMapper).toResponseDto(updatedUser);
    }

    @Test
    @DisplayName("사용자 업데이트 시 업데이트 데이터가 존재하지 않는 경우 예외를 반환한다.")
    void updateUserFailedCauseUpdateDataEmpty() throws IOException {
        // given
        UUID userId = UUID.randomUUID();
        User currentUser = mock(User.class);
        UserUpdateDto updateDto = new UserUpdateDto(
                null, null, null, null,
                null, null, null);

        given(userRepository.findById(userId)).willReturn(Optional.of(currentUser));
        given(userValidator.validateUpdate(currentUser, updateDto)).willReturn(null);

        // when & then
        Throwable thrown = Assertions.catchThrowable(() -> userService.update(userId, updateDto, null));

        Assertions.assertThat(thrown)
                .isInstanceOf(UserUpdateDataNotFoundException.class)
                .satisfies(e -> {
                    UserException ex = (UserUpdateDataNotFoundException) e;
                    Assertions.assertThat(ex.getDetails().get("userId")).isEqualTo(userId);
                    Assertions.assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_UPDATE_DATA_NOT_FOUND);
                });

        verify(userRepository).findById(userId);
        verify(userValidator).validateUpdate(currentUser, updateDto);
        verifyNoInteractions(binaryContentService);
    }

    @Test
    @DisplayName("프로필이 존재하지 않는 사용자를 성공적으로 삭제한다.")
    void userDeleteSuccess() {
        // given
        UUID userId = UUID.randomUUID();
        User deleteUser = mock(User.class);
        given(deleteUser.getId()).willReturn(userId);

        UserResponseDto responseDto = new UserResponseDto(
                userId, "구름소다", "cloudsoda@mail.com", null, true
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(deleteUser));
        willDoNothing().given(userRepository).delete(deleteUser);
        given(userMapper.toResponseDto(deleteUser)).willReturn(responseDto);

        // when
        UserResponseDto result = userService.delete(userId);

        // then
        Assertions.assertThat(result).isEqualTo(responseDto);
    }

    @Test
    @DisplayName("삭제하고자 하는 사용자를 찾지 못하는 경우 예외를 반환한다.")
    void userDeleteFaildCauseUserNotFound() {
        // given
        UUID userId = UUID.randomUUID();

        doThrow(new UserNotFoundException(userId))
                .when(userRepository).findById(userId);

        // when
        Throwable thrown = Assertions.catchThrowable(() -> userService.delete(userId));

        // then
        Assertions.assertThat(thrown)
                .isInstanceOf(UserNotFoundException.class)
                .satisfies(e -> {
                    UserException ex = (UserNotFoundException) e;
                    Assertions.assertThat(ex.getDetails().get("userId")).isEqualTo(userId);
                    Assertions.assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
                });
    }

}
