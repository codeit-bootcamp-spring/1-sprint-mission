package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Type.BinaryContentType;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.file.FileIOHandler;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.UUID;

public class BasicBinaryContentService {
    //todo 흑흑 시간안에 구현못함. 구현하기.........
    /*FileIOHandler fileIOHandler;
    BinaryContentRepository binaryContentRepository;

    //바이너리 객체 생성
    @Override
    public BinaryContent createBinaryContent(String profilePicturePath, BinaryContentType contentType, UUID userId) {
        if (profilePicturePath == null || contentType == null || userId == null) {return null;}
        //OI핸들러에 경로넘겨주고 그 경로에 있던 이미지 받아오기
        BufferedImage profilePicture = fileIOHandler.loadImage(profilePicturePath);
        if (profilePicture == null) {return null;}
        //받아온 이미지를 바이너리객체로 만들기
        BinaryContent binaryProfilePicture = new BinaryContent(userId, contentType, profilePicture);
        //프로필사진 바이너리객체를 바이너리레포지토리에 저장.
        if (binaryContentRepository.saveBinaryContent(binaryProfilePicture)==true){
            return binaryProfilePicture;
        }else{
            return null;
        }
    }

    //바이너리객체 리턴
    @Override
    public BinaryContent findBinaryContentById(UUID binaryContentId) {
        if (binaryContentId == null) {return null;}
        BinaryContent binaryContent = findBinaryContentById(binaryContentId);
        return binaryContent!=null ? binaryContent : null;
    }

    //채널에 존재하는 바이너리객체 리스트 리턴
    @Override
    public List<BinaryContent> findAllBinaryContentsByIdIn(UUID channelId) {
        return List.of();
    }

    @Override
    public Boolean deleteBinaryContentById(UUID binaryContentId) {
        return null;
    }
    */

}
