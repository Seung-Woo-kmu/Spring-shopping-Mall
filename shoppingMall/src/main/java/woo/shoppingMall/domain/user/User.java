package woo.shoppingMall.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import woo.shoppingMall.domain.BaseTimeEntity;
import woo.shoppingMall.domain.Address;
import woo.shoppingMall.web.dto.AddUserDto;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    public User(String userLoginId, String userPassword, String userName, String nickName, String userPhoneNumber, String userEmail, Address address, Authorization authorization) {
        this.userLoginId = userLoginId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.nickName = nickName;
        this.userPhoneNumber = userPhoneNumber;
        this.userEmail = userEmail;
        this.address = address;
        this.authorization = authorization;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String userLoginId;

    private String userPassword;

    private String userName;

    private String nickName;

    private String userPhoneNumber;

    private String userEmail;

    @Embedded
    private Address address;

    @Enumerated(value = EnumType.STRING)
    private Authorization authorization;


    public User(AddUserDto addUserDto) {
        this.id = addUserDto.getId();
        this.userLoginId = addUserDto.getUserLoginId();
        this.userPassword = addUserDto.getUserPassword();
        this.userName = addUserDto.getUserName();
        this.nickName = addUserDto.getNickName();
        this.userPhoneNumber = addUserDto.getUserPhoneNumber();
        this.userEmail = addUserDto.getUserEmail();
        this.address = new Address(addUserDto.getZipCode(), addUserDto.getStreet(), addUserDto.getDetail());
        this.authorization = Authorization.NORMAL;
    }
    public void makeAddress(Address address) {
        this.address = address;
    }
}
