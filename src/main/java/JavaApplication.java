
import com.sprint.mission.discodeit.dto.user.UserCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;


public class JavaApplication {


    public static void main(String[] args) {

        JCFUserRepository jcfUserRepository =new JCFUserRepository();
        BasicUserService basicUserService = new BasicUserService(jcfUserRepository);

        //DTO User생성
        User user = basicUserService.createUser(new UserCreateDTO("홍길동", "1234", "dis@code.it", ""));
        User user2 = basicUserService.createUser(new UserCreateDTO("김이박", "1234", "dis2@code.it", ""));
        System.out.println(basicUserService.findUserDTO(user.getId()).getFilePath());

        //User update
        basicUserService.updateUser(user.getId(),new UserUpdateDTO("홍감동", "1234", "dis@code.it", "filePath" ));

        System.out.println(basicUserService.findUserDTO(user.getId()).getName());
        System.out.println(basicUserService.findUserDTO(user.getId()).getFilePath());

        System.out.println(basicUserService.isNameExist("홍감동"));
        System.out.println(basicUserService.findAllUserDTO());

        //delete
        basicUserService.deleteUser(user2.getId());
        System.out.println(basicUserService.findAllUserDTO());



//        //Basic*Service 테스트
//        JCFChannelRepository jcfChannelRepository = new JCFChannelRepository();
//        BasicChannelService basicChannelService = new BasicChannelService(jcfChannelRepository);

//        //채널 추가
//        Channel newChannel = basicChannelService.createChannel("newChannel");
//        Channel newChannel2 = basicChannelService.createChannel("newChannel2");
//        System.out.println(basicChannelService.readChannel(newChannel.getId()).getChannelName());
//        System.out.println(basicChannelService.readChannel(newChannel2.getId()).getChannelName());
//
//        //모두 로드
//        System.out.println(basicChannelService.readAllChannel());
//
//        //이름 변경
//        System.out.println(basicChannelService.modifyChannel(newChannel2.getId(), "newChannel3"));
//        System.out.println(basicChannelService.readChannel(newChannel2.getId()).getChannelName());
//
//        //채널 삭제
//        basicChannelService.deleteChannel(newChannel2.getId());
//        System.out.println(basicChannelService.readAllChannel());
//



    }
        
}
