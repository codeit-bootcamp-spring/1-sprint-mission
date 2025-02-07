package com.sprint.mission.discodeit.service.domain;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryContentService {
    //이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델입니다. 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용합니다.
    private final BinaryContentRepository binaryContentRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public BinaryContent save(BinaryContentDto binaryContentDto) {
        if (binaryContentDto.domainId() == null || binaryContentDto.file() == null) {
            throw new IllegalStateException("도메인 아이디 혹은 파일이 잘못되었습니다.");
        }
        return binaryContentRepository.save(binaryContentDto);
    }

    public BinaryContent findById(UUID id) {
        return binaryContentRepository.findById(id);
    }

    public List<BinaryContent> findByDomainId(UUID domainId) {
        if (userRepository.findUserById(domainId) != null) {
            //Domain -> User
            return binaryContentRepository.findByDomainId(domainId);
        }
        if (messageRepository.findMessagesById(domainId) == null) {
            //Domain -> Message
            return binaryContentRepository.findByDomainId(domainId);
        }
        throw new IllegalStateException("도메인을 찾을 수 없습니다.");
    }

    public List<BinaryContent> findAll() {
        return binaryContentRepository.findAll();
    }


    public void delete(UUID id) {
        if (binaryContentRepository.findById(id) != null) {
            binaryContentRepository.delete(id);
        }
        throw new IllegalStateException("아이디를 찾을 수 없습니다.");
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
