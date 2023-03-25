package woo.shoppingMall.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindUserDto {
    @NotBlank
    private String userName;

    @Email
    @NotBlank
    private String userEmail;
}
