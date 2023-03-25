package woo.shoppingMall.domain.order;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import woo.shoppingMall.domain.Address;
import woo.shoppingMall.domain.item.Item;
import woo.shoppingMall.domain.item.ItemCategory;
import woo.shoppingMall.domain.item.ItemInfo;
import woo.shoppingMall.domain.item.ItemInfoId;
import woo.shoppingMall.repository.ItemInfoRepository;
import woo.shoppingMall.service.ItemService;
import woo.shoppingMall.domain.user.Authorization;
import woo.shoppingMall.domain.user.User;
import woo.shoppingMall.service.UserService;
import woo.shoppingMall.service.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class OrderServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;
    @Autowired
    OrderService orderService;
    @Autowired
    ItemInfoRepository itemInfoRepository;

/*    @Test
    public void orderTest() {
        User userA = new User("c","a","c","010", "a@naver.com",new Address("a", "a", "a"), Authorization.NORMAL);
        User userB = new User("d","b","d","010", "b@naver.com",new Address("b", "b", "b"), Authorization.NORMAL);
        userService.addUser(userA);
        userService.addUser(userB);
        User user1 = userService.findOne(1L).get();
        User user2 = userService.findOne(2L).get();

        Item itemA = new Item("a", "편한 운동화입니다.", 30000, ItemCategory.SNEAKERS, "a", "shoes/blackSneakers.jpg");
        Item itemB = new Item("a", "편한 구두입니다.", 40000, ItemCategory.HIGH_SHOES, "b", "shoes/highHeel.jpg");
        itemService.addItem(itemA);
        itemService.addItem(itemB);

        ItemInfoId itemInfoIdA = new ItemInfoId(itemA.getId(), 300);
        ItemInfo itemInfoA = new ItemInfo(itemA, itemInfoIdA, 1000);
        ItemInfoId itemInfoIdA1 = new ItemInfoId(itemA.getId(), 310);
        ItemInfo itemInfoA1 = new ItemInfo(itemA, itemInfoIdA1, 500);
        ItemInfoId itemInfoIdA2 = new ItemInfoId(itemA.getId(), 320);
        ItemInfo itemInfoA2 = new ItemInfo(itemA, itemInfoIdA2, 500);

        ItemInfoId itemInfoIdB = new ItemInfoId(itemB.getId(), 300);
        ItemInfo itemInfoB = new ItemInfo(itemB, itemInfoIdB, 10);

        ItemInfoId itemInfoIdB1 = new ItemInfoId(itemB.getId(), 310);
        ItemInfo itemInfoB1 = new ItemInfo(itemB, itemInfoIdB1, 10);

        itemInfoRepository.save(itemInfoA);
        itemInfoRepository.save(itemInfoA1);
        itemInfoRepository.save(itemInfoA2);
        itemInfoRepository.save(itemInfoB);
        itemInfoRepository.save(itemInfoB1);


        List<ItemInfo> items = new ArrayList<>();
        items.add(itemInfoA);
        items.add(itemInfoA1);

        List<Integer> counts = new ArrayList<>();
        counts.add(4);
        counts.add(10);
        Long orderId = orderService.order(user1.getId(), itemA.getId(), items, counts);
        Order getOrder = orderService.findOne(orderId).get();

        assertEquals(OrderStatus.ORDER, getOrder.getOrderStatus());
        assertEquals(2, getOrder.getOrderItems().size());
        assertEquals(996, itemInfoA.getStockQuantity());
        assertEquals(490, itemInfoA1.getStockQuantity());
        assertEquals(420000, getOrder.getOrderPrice());


        orderService.cancelOrder(orderId);
        assertEquals(500, itemInfoA1.getStockQuantity());
    }*/
}