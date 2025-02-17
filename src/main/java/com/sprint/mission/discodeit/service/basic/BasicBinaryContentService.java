package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Dto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.Type.BinaryContentType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.file.FileIOHandler;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {
    FileIOHandler fileIOHandler;
    BinaryContentRepository binaryContentRepository;
    UserService userService;

    public BinaryContent createBinaryContent(String filePath, BinaryContentType contentType, UUID userId){
        return createBinaryContent(filePath, contentType, userId, null);
    }
                                                //바이너리 객체 생성.
    @Override
    public BinaryContent createBinaryContent(String filePath, BinaryContentType contentType, UUID userId, UUID channelId) {
        if (filePath == null || contentType == null || userId == null) {return null;}
        //OI핸들러에 경로넘겨주고 그 경로에 있던 이미지 받아오기
        BufferedImage uploadedPicture = fileIOHandler.loadImage(filePath);
        if (uploadedPicture == null) {System.out.println("바이너리 객체 생성 실패. 이미지를 불러오지 못했습니다."); return null;}
        //받아온 이미지를 바이너리객체로 만들기
        BinaryContent binaryProfilePicture = new BinaryContent(userId, contentType, uploadedPicture);
        //프로필사진 바이너리객체를 바이너리레포지토리에 저장.
        if(binaryContentRepository.addBinaryContent(contentType, channelId, binaryProfilePicture)==false){System.out.println("바이너리 객체 생성 실패."); return null;}
        return binaryProfilePicture;
    }

    @Override
    public BinaryContent findAttachedContentById(UUID channelId, UUID binaryContentId) {
        if (channelId ==null || binaryContentId == null) {return null;}
        BinaryContent binaryContent = binaryContentRepository.getBinaryContent(BinaryContentType.Aettached_File, channelId, binaryContentId);
        if (binaryContent == null) {System.out.println("바이너리 객체 리턴 실패."); return null;}
        return binaryContent;
    }

    @Override
    public BinaryContent findProfilePictureById(UUID userId) {
        if (userId == null) {return null;}
        BinaryContent binaryContent = binaryContentRepository.getBinaryContent(BinaryContentType.Profile_Picture, null, userService.findUserById(userId).profilePictureId());
        if (binaryContent == null) {System.out.println("바이너리 객체 리턴 실패."); return null;}
        return binaryContent;
    }

    @Override
    public List<BinaryContentDto> findAllBinaryContentsByChannelId(UUID channelId) {
        if (channelId == null) {return null;}
        LinkedHashMap<UUID, BinaryContent> binaryContentsMap= binaryContentRepository.getBinaryContentsMap(BinaryContentType.Profile_Picture, channelId);
        if (binaryContentsMap == null){return null;}
        return !binaryContentsMap.isEmpty() ? binaryContentsMap.values().stream().map(binaryContent -> BinaryContentDto.from(binaryContent)).collect(Collectors.toList()) : null;
    }

    @Override
    public Boolean deleteAttachedContentById(UUID binaryContentId, UUID channelId) {
        return binaryContentRepository.deleteBinaryContent(BinaryContentType.Aettached_File, channelId, binaryContentId);
    }

    @Override
    public Boolean deleteProfilePictureById(UUID binaryContentId, UUID userId) {
        return binaryContentRepository.deleteBinaryContent(BinaryContentType.Profile_Picture, userId, null);
    }


}
