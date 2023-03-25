package woo.shoppingMall.web.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class LoginDto {
    @NotBlank
    private String userLoginId;

    @NotBlank
    private String userPassword;

}
