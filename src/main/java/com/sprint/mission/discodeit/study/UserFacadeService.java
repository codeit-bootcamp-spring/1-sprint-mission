//package com.sprint.mission.discodeit.study;
//
//import com.sprint.mission.discodeit.dto.user.request.CreateUserRequest;
//import com.sprint.mission.discodeit.dto.user.response.FindUserResponse;
//import com.sprint.mission.discodeit.entity.BinaryContent;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.entity.UserStatus;
//import com.sprint.mission.discodeit.mapper.UserMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class UserFacadeService {
//
//    private final TempUserService userService;
//    private final TempUserStatusService userStatusService;
//    private final TempBinaryContentService binaryContentService;
//    private final UserMapper userMapper;
//
//    public User createUser(CreateUserRequest createUserRequest, MultipartFile profileImageFile) {
//        User user = userService.createUser(createUserRequest);
//
//        // 이미지는 선택적으로 등록
//        updateProfileImage(user, profileImageFile);
//
//        userStatusService.createUserStatus(user.getId());
//
//        return user; // mapper 로 dto 변환
//    }
//
//    public FindUserResponse findUserById(UUID userId) {
//        User foundUser = userService.findUserByIdOrThrow(userId);
//        UserStatus userStatus = userStatusService.findUserStatusByUser(foundUser);
//
//        byte[] profileImage = foundUser.getProfileImage().getContent();
//        return userMapper.toFindUserResponse(foundUser, profileImage, userStatus.isOnline());
//    }
//
//    public List<FindUserResponse> findAllUsers() {
//        return userService.findAllUsers().stream()
//                .map(user -> findUserById(user.getId()))
//                .collect(Collectors.toList());
//    }
//
////    public User updateUser(UpdateUserRequest updateUserRequest, MultipartFile profileImageFile) {
////        User updatedUser = userService.updateUser(updateUserRequest, profileImageFile);
////
////        updateProfileImage(updatedUser, profileImageFile);
////
////        return updatedUser;
////    }
//
//    public void deleteUser(UUID userId) {
//        // 유저 존재 여부 확인
//        User foundUser = userService.findUserByIdOrThrow(userId);
//
//        // UserStatus 삭제
//        UserStatus userStatus = userStatusService.findUserStatusByUser(foundUser);
//        userStatusService.deleteUserStatus(userStatus.getId());
//
//        // BinaryContent 삭제
//        binaryContentService.deleteBinaryContent(foundUser.getProfileImage().getId());
//
//        // 유저 삭제
//        userService.deleteUser(foundUser.getId());
//    }
//
//    private void updateProfileImage(User user, MultipartFile profileImageFile) {
//        if (profileImageFile != null) {
//            BinaryContent binaryContent = binaryContentService.createBinaryContent(profileImageFile);
//            user.updateProfileImage(binaryContent);
//        }
//    }
//}
