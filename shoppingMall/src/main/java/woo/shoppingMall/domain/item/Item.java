package woo.shoppingMall.domain.item;

import jakarta.persistence.*;
import lombok.*;
import woo.shoppingMall.domain.BaseTimeEntity;
import woo.shoppingMall.domain.delivery.Delivery;
import woo.shoppingMall.domain.order.Order;
import woo.shoppingMall.domain.order.OrderStatus;
import woo.shoppingMall.domain.user.User;
import woo.shoppingMall.web.dto.ItemEditDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String itemName;

    private String itemDetail;

    private int price;

    private String filePath;

    @Enumerated(EnumType.STRING)
    private ItemCategory itemCategory;

    public Item(String itemName, String itemDetail, int price, ItemCategory itemCategory, String filePath) {
        this.itemCategory = itemCategory;
        this.itemDetail = itemDetail;
        this.itemName = itemName;
        this.price = price;
        this.filePath = filePath;
    }
    public Item(ItemEditDto itemEditDto, String filePath) {
        this.id = itemEditDto.getId();
        this.itemName = itemEditDto.getItemName();
        this.itemDetail = itemEditDto.getItemDetail();
        this.itemCategory = itemEditDto.getItemCategory();
        this.price = itemEditDto.getPrice();
        this.filePath = filePath;
    }
}
