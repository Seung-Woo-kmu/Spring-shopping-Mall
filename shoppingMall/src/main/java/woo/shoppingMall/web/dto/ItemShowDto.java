package woo.shoppingMall.web.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import woo.shoppingMall.domain.item.Item;
import woo.shoppingMall.domain.item.ItemCategory;

@Getter
@Setter
@AllArgsConstructor
public class ItemShowDto {
    private Long id;

    private String itemName;

    private String itemDetail;

    private int price;

    private String filePath;

    private ItemCategory itemCategory;

    private Integer reviewCount;

    private Integer reviewAverage;

    public ItemShowDto(Item item, Integer count, Integer average) {
        this.id = item.getId();
        this.itemName = item.getItemName();
        this.itemDetail = item.getItemDetail();
        this.price = item.getPrice();
        this.filePath = item.getFilePath();
        this.itemCategory = item.getItemCategory();
        this.reviewCount = count;
        this.reviewAverage = average;
    }
}
