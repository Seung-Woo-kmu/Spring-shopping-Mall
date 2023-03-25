package woo.shoppingMall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woo.shoppingMall.domain.user.Authorization;
import woo.shoppingMall.domain.user.User;
import woo.shoppingMall.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void addUser(User user) {
        userRepository.save(user);
    }

    public List<User> findUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findOne(Long userId) {
        return userRepository.findById(userId);
    }

    //일반 회원 로그인
    public User login(String userLoginId, String userPassword) {
        return userRepository.findByUserLoginId(userLoginId)
                .filter(u -> u.getAuthorization().equals(Authorization.NORMAL))
                .filter(u -> u.getUserPassword().equals(userPassword))
                .orElse(null);
    }
    //이름과 이메일로 회원 찾기
    public User findByName(String userName, String userEmail) {
        return userRepository.findByUserName(userName)
                .filter(u -> u.getUserEmail().equals(userEmail))
                .orElse(null);
    }
    //관리자 로그인
    public User adminLogin(String userLoginId, String userPassword) {
        return userRepository.findByUserLoginId(userLoginId)
                .filter(u -> u.getAuthorization().equals(Authorization.ADMIN))
                .filter(u -> u.getUserPassword().equals(userPassword))
                .orElse(null);
    }
}
