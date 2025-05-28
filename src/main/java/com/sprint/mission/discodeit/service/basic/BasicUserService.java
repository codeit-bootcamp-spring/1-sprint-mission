package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.auth.CustomUserDetails;
import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.auth.Role;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.file.FileUploadFailedException;
import com.sprint.mission.discodeit.exception.file.InvalidFileDataException;
import com.sprint.mission.discodeit.exception.user.DuplicatedEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicatedUsernameException;
import com.sprint.mission.discodeit.exception.user.InvalidUserInputException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserMapper userMapper;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;
    private final PasswordEncoder passwordEncoder;
    private UUID currentSessionUserId;

    @Override
    @Transactional
    public UserDto createUser(UserCreateRequestDto request, MultipartFile profile) {
        log.info("Create user: name={},email={},password={}", request.getUsername(),
                request.getEmail(),
                request.getPassword());
        if (request.getUsername() == null || request.getEmail() == null
                || request.getPassword() == null) {
            log.warn("Invalid request: name={},email={},password={}", request.getUsername(),
                    request.getEmail(), request.getPassword());
            throw new InvalidUserInputException();
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Email already exists: {}", request.getEmail());
            throw new DuplicatedEmailException();
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Username already exists: {}", request.getUsername());
            throw new DuplicatedUsernameException();
        }

        BinaryContent profileImage =
                (profile != null && !profile.isEmpty()) ? saveProfile(profile) : null;

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(request.getUsername(), request.getEmail(), encodedPassword,
                profileImage);
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
        log.debug("Saved user: id={}, email={}", user.getId(), user.getEmail());

        return userMapper.toDto(user);
    }


    @Override
    public UserDto getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> all = userRepository.findAll();
        UUID currentUserId = getCurrentSessionUserId();
        return all.stream().map(user -> {
            UserDto dto = userMapper.toDto(user);
            dto.setOnline(user.getId().equals(currentUserId));
            return dto;
        }).toList();
    }


    @Override
    @Transactional
    public UserDto updateUser(UUID userId, UserUpdateRequestDto request, MultipartFile profile) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        log.info("Update user: id={}, email={}", userId, user.getEmail());
        BinaryContent oldProfile = user.getProfile();

        if (profile != null && !profile.isEmpty()) {
            if (oldProfile == null || !oldProfile.getFileName()
                    .equals(profile.getOriginalFilename())) {
                log.debug("profile update: old={}, new={}", oldProfile, profile);
                if (oldProfile != null) {
                    getFileExtension(oldProfile.getFileName());
                    binaryContentRepository.deleteById(oldProfile.getId());
                }

                BinaryContent newProfile = saveProfile(profile);
                user.setProfile(newProfile);
            }
        }
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());

        user.update(request.getNewUsername(), request.getNewEmail(), encodedPassword);

        return userMapper.toDto(user);
    }


    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        log.info("user deleted: id={}, email={}", userId, user.getEmail());
        if (user.getProfile() != null) {
            binaryContentRepository.delete(user.getProfile());
        }

        userRepository.delete(user);
        log.debug("Deleted user o: id={}, email={}", userId, user.getEmail());
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex > 0) ? fileName.substring(dotIndex) : "";
    }

    private BinaryContent saveProfile(MultipartFile profileFile) {
        if (profileFile == null || profileFile.isEmpty()) {
            throw new InvalidFileDataException();
        }

        BinaryContent binaryContent = new BinaryContent(
                profileFile.getOriginalFilename(),
                profileFile.getSize(),
                profileFile.getContentType()
        );

        BinaryContent savedContent = binaryContentRepository.save(binaryContent);

        try {
            binaryContentStorage.put(savedContent.getId(), profileFile.getBytes());
        } catch (IOException e) {
            throw new FileUploadFailedException();
        }

        return savedContent;
    }

    @Override
    @Transactional
    public BinaryContentDto saveProfileImage(MultipartFile profileFile) {
        if (profileFile == null || profileFile.isEmpty()) {
            throw new NoSuchElementException("multipartFile is null or empty");
        }
        BinaryContent binaryContent = new BinaryContent(
                profileFile.getOriginalFilename(),
                profileFile.getSize(),
                profileFile.getContentType()
        );
        BinaryContent savedContent = binaryContentRepository.save(binaryContent);

        try {
            binaryContentStorage.put(savedContent.getId(), profileFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return binaryContentMapper.toDto(savedContent);
    }

    public UUID getCurrentSessionUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUser().getId();
        }
        return null;
    }
}
