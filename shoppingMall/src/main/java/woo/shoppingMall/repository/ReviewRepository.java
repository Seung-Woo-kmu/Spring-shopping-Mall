package woo.shoppingMall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import woo.shoppingMall.domain.Review;
import woo.shoppingMall.domain.item.Item;
import woo.shoppingMall.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Override
    @Query("select r from Review r left join fetch r.user")
    List<Review> findAll();
    List<Review> findAllByItem(Item item);

    List<Review> findAllByItemOrderByGradeDesc(Item item);

    List<Review> findAllByUser(User user);

    List<Review> findAllByItemOrderByLastModifiedDateDesc(Item item);

    List<Review> deleteByItemId(Long itemId);
}
