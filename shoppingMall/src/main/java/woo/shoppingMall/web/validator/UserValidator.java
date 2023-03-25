package woo.shoppingMall.web.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import woo.shoppingMall.domain.user.User;
import woo.shoppingMall.repository.UserRepository;
import woo.shoppingMall.web.dto.AddUserDto;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final UserRepository userRepository;

    public static final String patternAll =  "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{10,20}$";


    @Override
    public boolean supports(Class<?> clazz) {
        return AddUserDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AddUserDto user = (AddUserDto) target;
        String userPassword = user.getUserPassword();
        Matcher matcher = Pattern.compile(patternAll).matcher(userPassword);
        if (!matcher.find()) {
            errors.rejectValue("userPassword", "passwordError", "비밀번호는 10~20자이며 최소 하나의 영문, 숫자, 특수문자를 포함해야 합니다.");
        }
        Optional<User> loginUser = userRepository.findByUserLoginId(user.getUserLoginId());
        Optional<User> user2 = loginUser.filter(n -> n.getId().equals(user.getId()));
        if (loginUser.isPresent() && user2.isEmpty()) {
            errors.rejectValue("userLoginId", "loginIdError", "이미 존재하는 아이디 입니다.");
        }
        Optional<User> existUser = userRepository.findByNickName(user.getNickName());
        Optional<User> sameUser = loginUser.filter(n -> n.getId().equals(user.getId()));
        if (existUser.isPresent() && sameUser.isEmpty()) {
            errors.rejectValue("nickName", "nickNameError", "이미 존재하는 닉네임 입니다.");
        }
    }
}
