package woo.shoppingMall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import woo.shoppingMall.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserLoginId(String userLoginId);

    Optional<User> findByUserName(String userName);

    Optional<User> findByNickName(String nickName);
}
