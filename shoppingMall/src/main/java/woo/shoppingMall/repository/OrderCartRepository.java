package woo.shoppingMall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import woo.shoppingMall.domain.Cart;
import woo.shoppingMall.domain.order.OrderCart;

import java.util.List;

public interface OrderCartRepository extends JpaRepository<OrderCart, Long> {
    List<OrderCart> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}
