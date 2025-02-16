package com.sprint.mission.discodeit.repository.file;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileManager<T> {
    private final Path FILE_PATH;

    public FileManager(String filePathInRoot) {
        FILE_PATH = Paths.get(System.getProperty("user.dir"), "tmp" , filePathInRoot);
    }

    public void saveListToFile(List<T> saveList) {
        try (
                FileOutputStream fos = new FileOutputStream(FILE_PATH.toFile(),false);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            for (T object : saveList) {
                oos.writeObject(object);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> loadListToFile() {
        List<T> data = new ArrayList<>();
        if(!Files.exists(FILE_PATH)) {
            return data;
        }
        try (
                FileInputStream fis = new FileInputStream(FILE_PATH.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            while (true) {
                T serObject = (T) ois.readObject();
                data.add(serObject);
            }
        } catch (EOFException e) {
//            System.out.println("read all objects");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
