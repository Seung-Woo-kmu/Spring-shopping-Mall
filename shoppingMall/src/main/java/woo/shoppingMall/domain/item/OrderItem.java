package woo.shoppingMall.domain.item;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import woo.shoppingMall.domain.BaseTimeEntity;
import woo.shoppingMall.domain.order.Order;

@Getter
@Entity
@Table(name = "order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "item_id"),
            @JoinColumn(name = "size")
    })
    private ItemInfo itemInfo;

    private int orderPrice;

    private int orderCount;

    public OrderItem(ItemInfo itemInfo, int orderPrice, int orderCount) {
        this.itemInfo = itemInfo;
        this.orderPrice = orderPrice;
        this.orderCount = orderCount;
    }
    public void makeOrder(Order order) {
        this.order = order;
    }

    public void updateOrderItem(ItemInfo itemInfo) {
        this.itemInfo = itemInfo;
    }

    /*
    상품 주문
    재고 부족 시 해당 상품은 주문되지 않음
     */
    public static OrderItem makeOrderItem(ItemInfo itemInfo, int orderPrice, int orderCount) throws IllegalStateException{
        OrderItem orderItem = new OrderItem(itemInfo, orderPrice, orderCount);
        int remainQuantity = itemInfo.removeStockQuantity(orderCount);
        if (remainQuantity < 0) {
            itemInfo.addStockQuantity(orderCount);
            return null;
        }
        return orderItem;
    }
    // 주문 취소
    public void cancel() {
        this.itemInfo.addStockQuantity(orderCount);
    }
    public int getOrderItemPrice() {
        return this.orderPrice * this.orderCount;
    }
}
