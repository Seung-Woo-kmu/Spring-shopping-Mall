package woo.shoppingMall.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class AddUserDto {

    private Long id;

    @NotBlank
    private String userLoginId;

    @NotBlank
    private String userPassword;

    @NotBlank
    private String userName;

    @NotBlank
    private String nickName;

    @NotBlank
    private String userPhoneNumber;

    @Email
    @NotBlank
    private String userEmail;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String street;

    @NotBlank
    private String detail;
}
