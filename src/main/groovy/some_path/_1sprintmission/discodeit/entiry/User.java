package some_path._1sprintmission.discodeit.entiry;

import some_path._1sprintmission.discodeit.entiry.enums.DiscodeStatus;
import some_path._1sprintmission.discodeit.entiry.enums.UserType;
import some_path._1sprintmission.discodeit.entiry.validation.Email;
import some_path._1sprintmission.discodeit.entiry.validation.Phone;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class User extends BaseEntity implements Serializable{

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

    public String getIntroduce(){
        return introduce;
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

    public void setUserPhone(String userPhone) {
        if (isValidPhoneNumber(userPhone)) {
            Phone updatephone = new Phone(userPhone);
            this.userPhone = updatephone;
            setUpdatedAt(System.currentTimeMillis() / 1000L);
        } else {
            throw new IllegalArgumentException("Invalid phone number format: " + userPhone);
        }
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