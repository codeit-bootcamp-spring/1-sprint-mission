package com.sprint.mission.discodeit.service.domain;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryContentService {
    //이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델입니다. 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용합니다.
    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public BinaryContentDto save(BinaryContentDto binaryContentDto) {
        if (binaryContentDto.domainId() == null || binaryContentDto.multipartFile() == null) {
            throw new IllegalStateException("도메인 아이디 혹은 파일이 잘못되었습니다.");
        }
        return new BinaryContentDto(binaryContentRepository.save(binaryContentDto));
    }

    public BinaryContentDto findById(UUID id) {
        return new BinaryContentDto(binaryContentRepository.findById(id));
    }

    public List<BinaryContentDto> findByDomainId(UUID domainId) {
        if(userRepository.findUserById(domainId) != null || messageRepository.findMessagesByChannelId(domainId) != null){ //findMessagesByChannelId 오류나면 ~ BySenderId
            List<BinaryContent> binaryContents = binaryContentRepository.findByDomainId(domainId);
            List<BinaryContentDto> binaryContentDtos = new ArrayList<>();
            for (BinaryContent binaryContent : binaryContents) {
                binaryContentDtos.add(new BinaryContentDto(binaryContent));
            }
            return binaryContentDtos;
        }
        throw new IllegalStateException("도메인을 찾을 수 없습니다.");
    }

    public List<BinaryContentDto> findAll() {
        List<BinaryContent> binaryContents = binaryContentRepository.findAll();
        List<BinaryContentDto> binaryContentDtos = new ArrayList<>();
        for (BinaryContent binaryContent : binaryContents) {
            binaryContentDtos.add(new BinaryContentDto(binaryContent));
        }
        return binaryContentDtos;
    }


    public void delete(UUID id) {
        if (findById(id) != null) {
            binaryContentRepository.delete(id);
        }else {
            throw new IllegalStateException("아이디를 찾을 수 없습니다.");
        }
    }

    public void deleteByDomainId(UUID domainId) {
        if (binaryContentRepository.findByDomainId(domainId) != null) {
            List<BinaryContent> binaryContents = binaryContentRepository.findByDomainId(domainId);
            for (BinaryContent binaryContent : binaryContents) {
                binaryContentRepository.delete(binaryContent.getId());
            }
        }
        throw new IllegalStateException("도메인을 찾을 수 없습니다.");
    }
}
