package woo.shoppingMall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import woo.shoppingMall.domain.order.Order;
import woo.shoppingMall.domain.order.OrderStatus;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Override
    @Query("select o from Order o left join fetch o.user")
    List<Order> findAll();

    List<Order> findAllByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);
}
