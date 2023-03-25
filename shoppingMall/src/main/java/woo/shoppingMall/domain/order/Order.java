package woo.shoppingMall.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import woo.shoppingMall.domain.BaseTimeEntity;
import woo.shoppingMall.domain.delivery.Delivery;
import woo.shoppingMall.domain.delivery.DeliveryStatus;
import woo.shoppingMall.domain.item.ItemInfo;
import woo.shoppingMall.domain.item.OrderItem;
import woo.shoppingMall.domain.user.User;
import woo.shoppingMall.web.dto.OrderDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.makeOrder(this);
    }
    public void makeDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.makeOrder(this);
    }
    public void addUser(User user){
        this.user = user;
    }
    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
    public void MakeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    // 상품 주문
    public static Order makeOrder(User user, Delivery delivery, List<OrderItem> orderItems) {
        Order order = new Order(OrderStatus.ORDER);
        order.addUser(user);
        order.makeDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        return order;
    }
    // 주문 취소
    public void orderCancel() {
        if (delivery.getDeliveryStatus() == DeliveryStatus.COMPLETE) {
            throw new IllegalStateException("이미 배송이 완료된 상품입니다");
        }
        this.MakeOrderStatus(OrderStatus.CANCEL);
        delivery.makeDeliveryStatus(DeliveryStatus.CANCEL);
        for (OrderItem orderItem : this.orderItems) {
            orderItem.cancel();
        }
    }
    public OrderDto toOrderDto() {
        return new OrderDto(id, user, orderStatus, orderItems, delivery);
    }

    // 총 주문 가격
    public int getOrderPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : this.orderItems) {
            totalPrice += orderItem.getOrderItemPrice();
        }
        return totalPrice;
    }
    public int getOrderCount() {
        int totalCount = 0;
        for (OrderItem orderItem: this.orderItems) {
            totalCount += orderItem.getOrderCount();
        }
        return totalCount;
    }
}
