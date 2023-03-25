package woo.shoppingMall.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import woo.shoppingMall.domain.item.Item;
import woo.shoppingMall.domain.item.ItemInfo;
import woo.shoppingMall.domain.order.OrderCart;
import woo.shoppingMall.domain.user.User;
import woo.shoppingMall.web.dto.CartDto;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderAmount;

    private int size;

    public Cart(User user, Item item, int orderAmount, int size) {
        this.user = user;
        this.item = item;
        this.orderAmount = orderAmount;
        this.size = size;
    }
    public CartDto toEntity() {
        return new CartDto(id, item.getFilePath(), item.getItemName(),item.getPrice() ,size, orderAmount);
    }
    public OrderCart toOrderCart() {
        return new OrderCart(user, item, orderAmount, size);
    }
}
