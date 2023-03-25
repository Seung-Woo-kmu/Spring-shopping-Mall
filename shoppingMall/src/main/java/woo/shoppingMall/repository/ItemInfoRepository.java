package woo.shoppingMall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import woo.shoppingMall.domain.item.Item;
import woo.shoppingMall.domain.item.ItemCategory;
import woo.shoppingMall.domain.item.ItemInfo;
import woo.shoppingMall.domain.item.ItemInfoId;

import java.util.List;
import java.util.Optional;

public interface ItemInfoRepository extends JpaRepository<ItemInfo, ItemInfoId> {
    @Override
    @Query("select io from ItemInfo io left join fetch io.item")
    List<ItemInfo> findAll();
    List<ItemInfo> findAllByItemId(Long itemId);

    ItemInfo findByItemInfoId(ItemInfoId itemInfoId);

    ItemInfo findByItemIdAndItemInfoId(Long itemId, ItemInfoId itemInfoId);
}
