package woo.shoppingMall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import woo.shoppingMall.domain.item.Item;
import woo.shoppingMall.domain.item.ItemCategory;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findByItemName(String itemName);
    Optional<List<Item>> findAllByItemNameContains(String query);
    List<Item> findAllByItemCategory(ItemCategory itemCategory);

}
