package woo.shoppingMall.domain.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import woo.shoppingMall.domain.BaseTimeEntity;

@Entity
@NoArgsConstructor
@Getter
public class ItemInfo extends BaseTimeEntity {

    public ItemInfo(Item item, ItemInfoId itemInfoId, int stockQuantity) {
        this.item = item;
        this.itemInfoId = itemInfoId;
        this.stockQuantity = stockQuantity;
    }

    @MapsId("itemId")
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    //상품 아이디와 사이즈를 복합키로 설정 (한 상품에 여러 개의 사이즈가 있을 수 있음)
    @EmbeddedId
    private ItemInfoId itemInfoId;

    private int stockQuantity;

    // 주문 취소 시 재고 원상복구
    public void addStockQuantity(int stockQuantity) {
        this.stockQuantity += stockQuantity;
    }

    //주문 시 주문량만큼 재고 감소
    public int removeStockQuantity(int stockQuantity) {
        return this.stockQuantity -= stockQuantity;
    }
    public void makeItem(Item item) {
        this.item = item;
    }
}
