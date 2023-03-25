package woo.shoppingMall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woo.shoppingMall.domain.item.Item;
import woo.shoppingMall.domain.item.ItemCategory;
import woo.shoppingMall.domain.item.ItemInfo;
import woo.shoppingMall.repository.ItemRepository;
import woo.shoppingMall.web.dto.ItemEditDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;

    //새로운 상품 추가
    @Transactional
    public void addItem (Item item) {
        itemRepository.save(item);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    //검색창에서 상품 검색 결과 리턴 (카테고리 검색, 상품 이름 검색 시 유효한 결과 리턴)
    public List<Item> findItems(String query) {
        if (query.equals("운동화")) {
            return itemRepository.findAllByItemCategory(ItemCategory.SNEAKERS);
        }
        if (query.equals("슬리퍼")) {
            return itemRepository.findAllByItemCategory(ItemCategory.SLIPPER);
        }
        if (query.equals("구두") || query.equals("하이힐")) {
            return itemRepository.findAllByItemCategory(ItemCategory.HIGH_SHOES);
        }
        if (query.equals("부츠")) {
            return itemRepository.findAllByItemCategory(ItemCategory.BOOTS);
        }
        return itemRepository.findAllByItemNameContains(query).orElse(itemRepository.findAll());
    }
    public Optional<Item> findOne(Long itemId) {
        return itemRepository.findById(itemId);
    }

    public Item findByItemName(String itemName) {
        return itemRepository.findByItemName(itemName);
    }

    //상품 삭제
    @Transactional
    public void delete(Long itemId) {
        itemRepository.deleteById(itemId);
    }
}
