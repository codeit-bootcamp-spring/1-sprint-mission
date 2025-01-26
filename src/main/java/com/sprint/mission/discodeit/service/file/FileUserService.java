package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserRepository;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.sprint.mission.discodeit.service.file.FilePath.*;

public class FileUserService implements UserRepository {
    private final Map<UUID, User> data = new HashMap<>();


    @Override
    public void save(User user){
        // 유저 아이디, 유저 이메일, 유저 닉네임, 유지 패스워드, 유저 생성 시간
        String userDataCSV = (user.getId() + "," + user.getUserEmail() + "," + user.getUserNickName() +
                "," + user.getPassword() + "," + user.getCreatedAt() + "," + user.getUpdatedAt() + "\n");
        // append를 true로 하면 계속 저장될텐데, 이거 쌓이면 나중에 불러올때 문제가 생길거같은 느낌이.
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true));
             BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            boolean isContain = false;
            String line;
            while ((line = br.readLine()) != null) {

                String[] split = line.split(",");
                if (split[1].equals(user.getUserEmail())) {
                    System.out.println("해당 E-mail은 이미 저장되어 있습니다.");
                    isContain = true;
                    break;
                }
            }
            if (!isContain) {
                bw.write(userDataCSV);
                System.out.println("유저 정보 저장 성공");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String findById(String id){
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[1].equals(id)) {
                    System.out.println("========= 조회 성공 =========");
                    System.out.println("해당 사용자의 UUID= " + data[0]);
                    System.out.println("해당 사용자의 Email= " + data[1]);
                    System.out.println("해당 사용자의 NickName= " + data[2]);
                    System.out.println("해당 사용자의 Password= " + data[3]);
                    System.out.println("해당 사용자의 Created At= " + data[4]);
                    System.out.println("해당 사용자의 Updated At= " + data[5]);
                    return line;
                }
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        return line;
    }
    @Override
    public void findAll(){
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(stringBuilder.toString());
    }

    @Override
    public void update(String user) {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
             BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String byId = findById(user);
            String[] split = byId.split(",");
            save(User.createUser(split[1], split[2], split[3], true));
            delete(user);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
            BufferedReader brForDummy = new BufferedReader(new FileReader(DUMMY_FILE_PATH));
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true));
            BufferedWriter bwForDummy = new BufferedWriter(new FileWriter(DUMMY_FILE_PATH))) {
            // id를 비교해
            // 같으면 건너뛰고 dummy.txt 보내
            // 다 보내면 dummy.txt를 그대로 repository.txt
            String line;
            // dummy.txt로 일치하는 라인 제외 문장 다 보내기.
            while((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (!data[2].equals(id)) {
                    bwForDummy.write(line);
                    break;
                }
            }

            // dummy.txt에 있는 값을 repository.txt로 배끼기.
            while((line = brForDummy.readLine()) != null) {
                bw.write(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
