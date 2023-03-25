package woo.shoppingMall.web.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import woo.shoppingMall.domain.delivery.Delivery;
import woo.shoppingMall.domain.delivery.DeliveryStatus;
import woo.shoppingMall.domain.item.OrderItem;
import woo.shoppingMall.domain.order.OrderStatus;
import woo.shoppingMall.domain.user.User;
import woo.shoppingMall.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class OrderDto {

    private Long id;

    private User user;

    private OrderStatus orderStatus;

    private List<OrderItem> orderItems = new ArrayList<>();

    private Delivery delivery;

    public List<String> getOrderItemName() {
        return orderItems.stream().map(n -> n.getItemInfo().getItem().getItemName()).collect(Collectors.toList());
    }
    public List<Integer> getOrderItemSize() {
        return orderItems.stream().map(n -> n.getItemInfo().getItemInfoId().getSize()).collect(Collectors.toList());
    }
    public List<Integer> getOrderItemPrice() {
        return orderItems.stream().map(OrderItem::getOrderItemPrice).collect(Collectors.toList());
    }
    public List<Integer> getOrderItemCount() {
        return orderItems.stream().map(OrderItem::getOrderCount).collect(Collectors.toList());
    }
    public String getDeliveryStatus() {
        return delivery.getDeliveryStatus().getStatus();
    }

    public String getOrderStatus() {
        return orderStatus.getStatus();
    }
    public int getOrderPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            System.out.println(orderItem.getOrderPrice());
            totalPrice += orderItem.getOrderPrice() * orderItem.getOrderCount();
        }
        return totalPrice;
    }
}
