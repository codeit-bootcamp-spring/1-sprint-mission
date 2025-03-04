package some_path._1sprintmission.discodeit.entiry;

import lombok.Getter;
import lombok.Setter;
import some_path._1sprintmission.discodeit.entiry.enums.DiscodeStatus;
import some_path._1sprintmission.discodeit.entiry.enums.UserType;
import some_path._1sprintmission.discodeit.entiry.validation.Email;
import some_path._1sprintmission.discodeit.entiry.validation.Phone;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Getter
@Setter
public class User extends BaseEntity implements Serializable{

    private String username; // 닉네임
    private String password; //비밀번호 -> 방금 만들어서 다시 다 엎어야됨... 엄두가 안남
    private Integer discriminator; //디스코드 전용 코드 이름#+4자리 숫자
    private Boolean isVerified; //본인 인증여부
    private Email userEmail; //이메일
    private Phone userPhone; //폰 번호
    private UserType userType; //common, vip
    private String introduce; //자기 소개
    private DiscodeStatus discodeStatus; // 디스코드 상태(활성화, 비활성화, 알람금지, 수면상태)
    private final Set<Channel> channels; // 유저가 속한 채널 목록
    private String profileImageUrl; // 프로필 이미지 URL

    public User(String username, String password, Email email, Phone phone) {
        super();
        this.username = username;
        this.password = password;
        this.userEmail = email;
        this.userPhone = phone;
        this.userType = UserType.COMMON;
        this.discodeStatus = DiscodeStatus.ACTIVE;
        this.channels = new HashSet<>();
    }

    public void leaveChannel(Channel channel) {
        if (channels.remove(channel)) {
            System.out.println("**** " +  username + " leaved " + channel.getName() + " ****");
            channel.removeMember(this);
        }
    }

    private boolean isValidPhoneNumber(String phone) {
        // Example regex for phone validation (adjust based on your requirements)
        String phoneRegex = "^010-\\d{4}-\\d{4}$";
        return Pattern.matches(phoneRegex, phone);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", discriminator=" + discriminator +
                ", verified=" + isVerified +
                ", userEmail=" + userEmail +
                ", userPhone=" + userPhone +
                ", userType=" + userType +
                ", introduce=" + introduce +
                ", discodeStatus=" + discodeStatus +
                ", channels=" + channels +
                '}';
    }
}