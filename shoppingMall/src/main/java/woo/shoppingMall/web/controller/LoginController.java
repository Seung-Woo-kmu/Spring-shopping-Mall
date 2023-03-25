package woo.shoppingMall.web.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import woo.shoppingMall.domain.user.User;
import woo.shoppingMall.service.EmailService;
import woo.shoppingMall.service.UserService;
import woo.shoppingMall.web.dto.FindUserDto;
import woo.shoppingMall.web.dto.LoginDto;

import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    private final EmailService emailService;

    //로그인 화면
    @GetMapping("/login")
    public String login(@ModelAttribute LoginDto loginDto, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "login/login";
    }
    //세션을 통한 로그인 처리 - 아이디와 비밀번호가 맞지 않거나 형식이 올바르지 않은 경우 로그인 화면으로 되돌아감
    @PostMapping("/login")
    public String afterLogin(@Validated @ModelAttribute LoginDto loginDto, BindingResult bindingResult, @RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/login";
        }
        User loginUser = userService.login(loginDto.getUserLoginId(), loginDto.getUserPassword());
        if (loginUser == null) {
            bindingResult.rejectValue("userPassword", "loginFail", "아이디 또는 비밀번호가 잘못되었습니다.");
            return "login/login";
        }
        HttpSession session = request.getSession();
        session.setAttribute("loginUser", loginUser.getId());
        return "redirect:" + redirectURL;
    }
    //로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
    //관리자 로그인 화면
    @GetMapping("/admin/login")
    public String adminLogin(@ModelAttribute LoginDto loginDto){
        return "login/adminLogin";
    }

    //관리자 로그인 처리
    @PostMapping("/admin/login")
    public String admin(@Validated @ModelAttribute LoginDto loginDto, BindingResult bindingResult, @RequestParam(defaultValue = "/admin") String redirectURL, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/adminLogin";
        }
        User loginUser = userService.adminLogin(loginDto.getUserLoginId(), loginDto.getUserPassword());
        if (loginUser == null) {
            bindingResult.rejectValue("userPassword", "loginFail", "아이디 또는 비밀번호가 잘못되었습니다.");
            return "login/adminLogin";
        }
        HttpSession session = request.getSession();
        session.setAttribute("adminUser", loginUser.getId());
        return "redirect:" + redirectURL;
    }
}
