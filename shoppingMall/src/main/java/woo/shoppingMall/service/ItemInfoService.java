package woo.shoppingMall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woo.shoppingMall.domain.item.Item;
import woo.shoppingMall.domain.item.ItemCategory;
import woo.shoppingMall.domain.item.ItemInfo;
import woo.shoppingMall.domain.item.ItemInfoId;
import woo.shoppingMall.repository.ItemInfoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemInfoService {
    private final ItemInfoRepository itemInfoRepository;

    //상품 아이디로 그 상품의 사이즈와 재고 찾기
    public List<ItemInfo> findAllByItemId(Long itemId) {
        return itemInfoRepository.findAllByItemId(itemId);
    }

    //모든 상품의 사이즈와 재고 찾기
    public List<ItemInfo> findItems() {
        return itemInfoRepository.findAll();
    }

    //해당 상품의 사이즈와 재고 삭제
    @Transactional
    public void delete(ItemInfoId itemInfoId) {
        itemInfoRepository.deleteById(itemInfoId);
    }

    //해당 상품에 새로운 사이즈와 재고 추가
    @Transactional
    public void add(ItemInfo itemInfo) {
        itemInfoRepository.save(itemInfo);
    }

    public ItemInfo findById(ItemInfoId itemInfoId) {
        return itemInfoRepository.findByItemInfoId(itemInfoId);
    }

}
