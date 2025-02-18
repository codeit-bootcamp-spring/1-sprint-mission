package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.execption.bianryContent.BinaryContentNofFoundException;
import com.sprint.mission.discodeit.global.error.execption.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.global.util.MultipartFileConverter;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.file.FileBinaryContentRepository;
import com.sprint.mission.discodeit.repository.file.FileStorage;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BinaryContentServiceTest {

    private BinaryContentRepository binaryContentRepository;
    private MultipartFileConverter multipartFileConverter;
    private BinaryContentService binaryContentService;
    private FileStorage fileStorage;

    @BeforeEach
    void setUp() {
//        jcfSetUp();
        fileSetUp();
        multipartFileConverter = new MultipartFileConverter();
        binaryContentService = new BasicBinaryContentService(binaryContentRepository, multipartFileConverter);
    }

    @AfterEach
    void clean() {
        if (fileStorage != null) {
            fileStorage.clearDataDirectory();
        }
    }

    private void jcfSetUp() {
        binaryContentRepository = new JCFBinaryContentRepository();
    }

    private void fileSetUp() {
        fileStorage = new FileStorage();
        binaryContentRepository = new FileBinaryContentRepository(fileStorage);
    }

    private MultipartFile createMulipartFile() {
        return  new MockMultipartFile(
                "file",                          // 필드명 (폼 필드 이름)
                "test.txt",                      // 파일명
                "text/plain",                    // MIME 타입
                "Hello, World!".getBytes()       // 파일 내용 (바이트 배열)
        );
    }

    private BinaryContent createBinaryContent(MultipartFile multipartFile) {
        byte[] bytes = multipartFileConverter.toByteArray(multipartFile);
        BinaryContent binaryContent = BinaryContent.of(bytes);
        return binaryContentRepository.saveBinaryContent(binaryContent);
    }

    @Nested
    @DisplayName("BinaryContent 생성 테스트")
    class CreateBinaryContentTest {

        @Test
        @DisplayName("BinaryContent 생성 성공 by MultipartFile")
        void successByMultipartFile() {
            // given
            MultipartFile multipartFile = createMulipartFile();

            // when
            BinaryContent binaryContent = binaryContentService.createBinaryContent(multipartFile);

            // then
            byte[] multipartFileBytes = null;
            try {
                multipartFileBytes = multipartFile.getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byte[] content = binaryContent.getContent();

            assertEquals(multipartFileBytes, content);
        }

        @Test
        @DisplayName("BinaryContent 생성 성공 by BinaryContent")
        void successByBinaryContent() {
            // given
            MultipartFile multipartFile = createMulipartFile();
            byte[] bytes = multipartFileConverter.toByteArray(multipartFile);

            // when
            BinaryContent binaryContent = binaryContentService.createBinaryContent(BinaryContent.of(bytes));

            // then
            byte[] multipartFileBytes = null;
            try {
                multipartFileBytes = multipartFile.getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byte[] content = binaryContent.getContent();

            assertEquals(multipartFileBytes, content);
        }
    }

    @Nested
    @DisplayName("BinaryContent 단일 조회 테스트")
    class FindBinaryContentTest {

        @Test
        @DisplayName("BinaryContent 단일 조회 성공")
        void success() {
            // given
            MultipartFile multipartFile = createMulipartFile();
            BinaryContent binaryContent = createBinaryContent(multipartFile);

            // when
            BinaryContent foundedBinaryContent = binaryContentService.findBinaryContentById(binaryContent.getId());

            // then
            assertEquals(binaryContent, foundedBinaryContent);
        }

        @Test
        @DisplayName("BinaryContent 단일 조회 성공")
        void fail_ThrowNotFoundException() {
            // given
            MultipartFile multipartFile = createMulipartFile();
            createBinaryContent(multipartFile);

            UUID randomId = UUID.randomUUID();

            // when & then
            assertThatThrownBy(() -> binaryContentService.findBinaryContentById(randomId))
                    .isInstanceOf(BinaryContentNofFoundException.class)
                    .hasMessage(ErrorCode.BINARYCONTENT_NOT_FOUND.format("id: " + randomId));
        }
    }

    @Nested
    @DisplayName("BinaryContent 목록 조회 테스트")
    class FindAllBinaryContentsTest {

        @Test
        @DisplayName("BinaryContent 목록 조회 성공")
        void success() {
            // given
            MultipartFile multipartFile = createMulipartFile();
            BinaryContent binaryContent1 = createBinaryContent(multipartFile);
            BinaryContent binaryContent2 = createBinaryContent(multipartFile);
            BinaryContent binaryContent3 = createBinaryContent(multipartFile);

            List<UUID> binaryContentIdList = new ArrayList<>();
            binaryContentIdList.add(binaryContent1.getId());
            binaryContentIdList.add(binaryContent2.getId());
            binaryContentIdList.add(binaryContent3.getId());

            // when
            List<BinaryContent> binaryContentList = binaryContentService.findAllBinaryContentsById(binaryContentIdList);

            // then
            assertEquals(3, binaryContentList.size());
            assertTrue(binaryContentList.contains(binaryContent1));
            assertTrue(binaryContentList.contains(binaryContent2));
            assertTrue(binaryContentList.contains(binaryContent3));
        }
    }

    @Nested
    @DisplayName("BinaryContent 삭제 테스트")
    class DeleteBinaryContentTest {

        @Test
        @DisplayName("BinaryContent 샥제 성공")
        void success() {
            // given
            MultipartFile multipartFile = createMulipartFile();
            BinaryContent binaryContent = createBinaryContent(multipartFile);

            // when
            binaryContentService.deleteBinaryContent(binaryContent.getId());

            // then
            assertNull(binaryContentRepository.findBinaryContentById(binaryContent.getId()));
        }
    }
}