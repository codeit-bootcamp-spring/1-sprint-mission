package com.sprint.mission.discodeit.repository.file;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileManager<T> {
    private final Path filePath;

    public FileManager(String filePathInRoot) {
        filePath = Paths.get(System.getProperty("user.dir"), filePathInRoot);
    }

    public void saveListToFile(List<T> saveList) {
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile(),false);
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
        if(!Files.exists(filePath)) {
            return data;
        }
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
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
