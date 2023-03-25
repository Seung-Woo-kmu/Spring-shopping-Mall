package woo.shoppingMall.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CartDto {
    private Long id;

    private String filePath;

    private String itemName;

    private int price;

    private int size;

    private int orderAmount;
}
