package woo.shoppingMall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import woo.shoppingMall.domain.item.ItemInfo;
import woo.shoppingMall.domain.item.OrderItem;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByItemInfo(ItemInfo itemInfo);
}
