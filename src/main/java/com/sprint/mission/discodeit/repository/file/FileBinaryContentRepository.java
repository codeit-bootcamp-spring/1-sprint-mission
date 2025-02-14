package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.binaryContent.CreateBinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.file.FileService;

import java.nio.file.Path;
import java.util.List;

public class FileBinaryContentRepository implements BinaryContentRepository {

    private final Path binaryContentDirectory;

    //todo - 환경 변수(파일위치) 수정 필요
    public FileBinaryContentRepository(Path binaryContentDirectory) {
        this.binaryContentDirectory = binaryContentDirectory;
        FileService.init(this.binaryContentDirectory);
    }

    @Override
    public BinaryContent save(CreateBinaryContentDto createBinaryContentDto) {
        BinaryContent binaryContent = new BinaryContent(createBinaryContentDto.binaryImage(), createBinaryContentDto.createdAt());
        Path binarayContentPath = binaryContentDirectory.resolve(binaryContent.getId().concat(".ser"));
        FileService.save(binarayContentPath, binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent findById(String id) {
        Path binaryContentPath = binaryContentDirectory.resolve(id.concat(".ser"));
        List<BinaryContent> load = FileService.load(binaryContentPath);
        if (load.isEmpty()) {
            return null;
        }
        return load.get(0);
    }

    @Override
    public List<BinaryContent> findAll() {
        return FileService.load(binaryContentDirectory);
    }

    @Override
    public boolean delete(String id) {
        return FileService.delete(binaryContentDirectory.resolve(id.concat(".ser")));
    }
}
