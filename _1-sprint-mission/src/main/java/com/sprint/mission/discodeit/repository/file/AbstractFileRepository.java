package com.sprint.mission.discodeit.repository.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public abstract class AbstractFileRepository<T> {
    protected final Path DIRECTORY;
    protected final String EXTENSION = ".ser";

    public AbstractFileRepository(String directory) {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"),"file-data-map",directory);
        if(Files.notExists(DIRECTORY)){
            try{
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 실패: "+ directory,e);
            }
        }
    }
    protected Path resolvePath(UUID id){
        return DIRECTORY.resolve(id + EXTENSION);
    }
}
