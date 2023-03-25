package woo.shoppingMall.domain.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import woo.shoppingMall.domain.Address;
import woo.shoppingMall.service.UserService;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(value = false)
class UserRepositoryTest {
    @Autowired
    UserService userService;

    //@Test
    /*public void testUser() {
        User userC = new User("c","a","a","010", "a@naver.com",new Address("a", "a", "a"), Authorization.NORMAL);
        User userD = new User("d","b","b","010", "b@naver.com",new Address("b", "b", "b"), Authorization.NORMAL);
        userService.addUser(userC);
        userService.addUser(userD);

        User user1 = userService.findOne(userC.getId()).orElseGet(User::new);
        User user2 = userService.findOne(userD.getId()).orElseGet(User::new);

        List<User> users = userService.findUsers();

        Assertions.assertThat(userC.getId()).isEqualTo(user1.getId());
        Assertions.assertThat(userD.getId()).isEqualTo(user2.getId());
        Assertions.assertThat(users.size()).isEqualTo(4);

    }*/
}