package woo.shoppingMall.web.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import woo.shoppingMall.domain.Address;
import woo.shoppingMall.domain.Review;
import woo.shoppingMall.domain.user.Authorization;
import woo.shoppingMall.domain.user.User;
import woo.shoppingMall.service.*;
import woo.shoppingMall.web.dto.AddUserDto;
import woo.shoppingMall.web.dto.FindUserDto;
import woo.shoppingMall.web.validator.UserValidator;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final ItemService itemService;
    private final EmailService emailService;
    private final UserService userService;

    private final ReviewService reviewService;

    private final UserValidator userValidator;

    @InitBinder("addUserDto")
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(userValidator);
    }

    @GetMapping("/addUser")
    public String add(@ModelAttribute AddUserDto addUserDto) {
        return "/user/addUserForm";
    }

    @PostMapping("/addUser")
    public String addUser(@Validated @ModelAttribute AddUserDto addUserDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "user/addUserForm";
        }
        Address address = new Address(addUserDto.getZipCode(), addUserDto.getStreet(), addUserDto.getDetail());
        User user = new User(addUserDto.getUserLoginId(), addUserDto.getUserPassword(), addUserDto.getUserName(), addUserDto.getNickName(), addUserDto.getUserPhoneNumber(), addUserDto.getUserEmail(), address, Authorization.NORMAL);
        userService.addUser(user);
        return "redirect:/";
    }
    @GetMapping("/mailCheck")
    @ResponseBody
    public String mailCheckGet(String email) throws Exception {
        log.info("이메일전송");
        String authNumber = emailService.sendEmail(email);
        return authNumber;
    }
    @GetMapping("/userInfo")
    public String myPage(@SessionAttribute(value = "loginUser", required = false) Long userId, Model model) {
        if (userId == null) {
            return "redirect:/";
        }
        User user = userService.findOne(userId).orElse(null);
        AddUserDto addUserDto = new AddUserDto(user.getId(), user.getUserLoginId(), user.getUserPassword(), user.getUserName(), user.getNickName(), user.getUserPhoneNumber(), user.getUserEmail(), user.getAddress().getZipCode(), user.getAddress().getStreet(), user.getAddress().getDetail());

        model.addAttribute("addUserDto", addUserDto);
        model.addAttribute("loginUser", userId);
        return "user/userInfo";
    }
    @PostMapping("/userInfo")
    public String updateUser(@SessionAttribute(value = "loginUser", required = false) Long userId, @Validated @ModelAttribute AddUserDto addUserDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (userId == null) {
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("loginUser", userId);
            log.info("errors={}", bindingResult);
            return "user/userInfo";
        }
        User user = new User(addUserDto);
        List<Review> reviews = reviewService.findByUser(user);
        for (Review review : reviews) {
            review.makeName(user.getUserName(), user.getNickName());
            reviewService.add(review);
        }
        userService.addUser(user);
        redirectAttributes.addAttribute("status", true);
        return "redirect:/userInfo";
    }
    @GetMapping("/find/id")
    public String findId(@ModelAttribute FindUserDto findUserDto) {
        return "user/find/id";
    }
    @PostMapping("/find/id")
    public String findUserId(@Validated @ModelAttribute FindUserDto findUserDto, BindingResult bindingResult) throws MessagingException, UnsupportedEncodingException {
        if (bindingResult.hasErrors()) {
            return "/user/find/id";
        }
        User user = userService.findByName(findUserDto.getUserName(), findUserDto.getUserEmail());
        if (user == null) {
            bindingResult.addError(new ObjectError("findId", "일치하는 대상이 없습니다."));
            return "/user/find/id";
        }
        emailService.sendUserIdEmail(findUserDto.getUserEmail(), user.getId());
        return "redirect:/find/complete";
    }
    @GetMapping("/find/complete")
    public String findIdComplete() {
        return "/user/find/complete";
    }
    @GetMapping("/find/password")
    public String findPassword(@ModelAttribute FindUserDto findUserDto) {
        return "user/find/password";
    }
    @PostMapping("/find/password")
    public String findUserPassword(@Validated @ModelAttribute FindUserDto findUserDto, BindingResult bindingResult) throws MessagingException, UnsupportedEncodingException {
        if (bindingResult.hasErrors()) {
            return "/user/find/password";
        }
        User user = userService.findByName(findUserDto.getUserName(), findUserDto.getUserEmail());
        if (user == null) {
            bindingResult.addError(new ObjectError("findPassword", "일치하는 대상이 없습니다."));
            return "/user/find/password";
        }
        emailService.sendUserPasswordEmail(findUserDto.getUserEmail(), user.getId());
        return "redirect:/find/complete";
    }
    @GetMapping("/admin/users")
    public String allUsers(Model model, @RequestParam(value = "sort", defaultValue = "0") String sortBy) {
        List<User> users;
        if (sortBy.equals("id")) {
            List<User> userList = new ArrayList<>(userService.findUsers().stream().sorted(Comparator.comparing(User::getUserName)).toList());
            Collections.reverse(userList);
            users = new ArrayList<>(userList);
        }
        else if (sortBy.equals("ii")) {
            users = new ArrayList<>(userService.findUsers().stream().sorted(Comparator.comparing(User::getUserName)).toList());
        }
        else {
            users = new ArrayList<>(userService.findUsers());
        }
        model.addAttribute("users", users);
        return "/admin/user/users";
    }
}
