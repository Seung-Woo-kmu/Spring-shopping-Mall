package woo.shoppingMall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woo.shoppingMall.domain.Address;
import woo.shoppingMall.domain.item.ItemInfo;
import woo.shoppingMall.domain.item.ItemInfoId;
import woo.shoppingMall.domain.order.Order;
import woo.shoppingMall.domain.delivery.Delivery;
import woo.shoppingMall.domain.delivery.DeliveryStatus;
import woo.shoppingMall.domain.order.OrderCart;
import woo.shoppingMall.domain.order.OrderStatus;
import woo.shoppingMall.repository.*;
import woo.shoppingMall.domain.item.OrderItem;
import woo.shoppingMall.domain.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final ItemInfoRepository itemInfoRepository;

    public Optional<Order> findOne(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findOrderByUserId(Long userId) throws IllegalStateException{
        return orderRepository.findAllByUserIdAndOrderStatus(userId, OrderStatus.ORDER);
    }

    //상품 주문
    @Transactional
    public Long order(Long userId, List<OrderCart> orderCarts){
        List<OrderItem> orderItems = new ArrayList<>();
        User user = userRepository.findById(userId).get();

        List<ItemInfo> items = new ArrayList<>();
        for (OrderCart orderCart : orderCarts) {
            ItemInfoId itemInfoId = new ItemInfoId(orderCart.getItem().getId(), orderCart.getSize());
            items.add(itemInfoRepository.findByItemIdAndItemInfoId(orderCart.getItem().getId(), itemInfoId));
        }

        //배송 상태 지정
        Delivery delivery = new Delivery();
        delivery.makeAddress(user.getAddress());
        delivery.makeDeliveryStatus(DeliveryStatus.READY);
        for (int i = 0; i < orderCarts.size(); i++) {
            OrderItem orderItem = OrderItem.makeOrderItem(items.get(i), items.get(i).getItem().getPrice(), orderCarts.get(i).getOrderAmount());
            if (orderItem == null) {
                continue;
            }
            orderItems.add(orderItem);
        }
        if (orderItems.size() != 0) {
            Order order = Order.makeOrder(user, delivery, orderItems);
            orderRepository.save(order);
            return order.getId();
        }
        return 0L;
    }

    //주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        order.orderCancel();
    }

    //주문 삭제
    @Transactional
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
    public List<OrderItem> findOrderItemByItemInfo(ItemInfo itemInfo) {
        return orderItemRepository.findAllByItemInfo(itemInfo);
    }

    //주문 상품 추가
    @Transactional
    public void addOrderItem(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    //주문 상품 삭제
    @Transactional
    public void deleteOrderItem(OrderItem orderItem) {
        orderItemRepository.delete(orderItem);
    }
}
