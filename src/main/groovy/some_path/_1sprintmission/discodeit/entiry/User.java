package some_path._1sprintmission.discodeit.entiry;

import some_path._1sprintmission.discodeit.entiry.enums.DiscodeStatus;
import some_path._1sprintmission.discodeit.entiry.enums.UserType;
import some_path._1sprintmission.discodeit.entiry.validation.Email;
import some_path._1sprintmission.discodeit.entiry.validation.Phone;

import java.util.HashSet;
import java.util.Set;

public class User extends BaseEntity {

    private String username; // 닉네임
    private Integer discriminator; //디스코드 전용 코드 이름#+4자리 숫자
    private Boolean isVerified; //본인 인증여부
    private Email userEmail; //이메일
    private Phone userPhone; //폰 번호
    private UserType userType; //common, vip
    private String introduce; //자기 소개
    private DiscodeStatus discodeStatus; // 디스코드 상태(현활, 비활, 알람금지, 수면상태)
    private final Set<Channel> channels; // 유저가 속한 채널 목록

    public User(String username, String email, String phone) {
        super();
        this.username = username;
        this.userEmail = new Email(email);
        this.userPhone = new Phone(phone);
        this.userType = UserType.COMMON;
        this.discodeStatus = DiscodeStatus.ACTIVE;
        this.channels = new HashSet<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
        setUpdatedAt(System.currentTimeMillis() / 1000L);
    }

    public Email getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(Email userEmail) {
        this.userEmail = userEmail;
        setUpdatedAt(System.currentTimeMillis() / 1000L);
    }

    public UserType getUserType(){
        return userType;
    }

    public void setUserType(UserType userType){
        this.userType = userType;
    }

    public UserType getIntroduce(){
        return userType;
    }

    public void setIntroduce(String introduce){
        this.introduce = introduce;
    }

    public DiscodeStatus getDiscodeStatus(){
        return discodeStatus;
    }

    public void setDiscodeStatus(DiscodeStatus discodeStatus){
        this.discodeStatus = discodeStatus;
    }

    public Phone getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(Phone userPhone) {
        this.userPhone = userPhone;
        setUpdatedAt(System.currentTimeMillis() / 1000L);
    }

    public Integer getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(Integer discriminator) {
        this.discriminator = discriminator;
    }

    public void setVerified(Boolean isVerified){
        this.isVerified = isVerified;
    }

    public Set<Channel> getChannels() {
        return channels;
    }

    public void joinChannel(Channel channel) {
        if (channels.add(channel)) {
            System.out.println("**** " + username + " joined " + channel.getName() + " ****");
            channel.addMember(this);
        }
    }

    public void leaveChannel(Channel channel) {
        if (channels.remove(channel)) {
            System.out.println("**** " +  username + " leaved " + channel.getName() + " ****");
            channel.removeMember(this);
        }
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", username='" + username + '\'' +
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