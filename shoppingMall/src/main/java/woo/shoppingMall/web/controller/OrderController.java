package woo.shoppingMall.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import woo.shoppingMall.domain.Address;
import woo.shoppingMall.domain.Cart;
import woo.shoppingMall.domain.item.Item;
import woo.shoppingMall.domain.item.ItemInfo;
import woo.shoppingMall.domain.item.OrderItem;
import woo.shoppingMall.domain.order.Order;
import woo.shoppingMall.domain.order.OrderCart;
import woo.shoppingMall.domain.user.User;
import woo.shoppingMall.service.*;
import woo.shoppingMall.web.dto.CartDto;
import woo.shoppingMall.web.dto.OrderDto;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final ItemService itemService;

    private final CartService cartService;

    private final OrderService orderService;

    private final UserService userService;

    private final OrderCartService orderCartService;

    //주문 화면 처리 - 장바구니에 담지 않고 바로 주문하는 상품은 주문 장바구니에 추가
    @PostMapping("/orderPage")
    public String orderPage(@RequestParam(value = "amount[]") List<Integer> amountList, @RequestParam(value = "size[]") List<Integer> sizeList, @RequestParam(value = "name") String itemName, @SessionAttribute(value = "loginUser", required = false) Long userId) {
        if (userId == null) {
            return "redirect:/";
        }
        Item item = itemService.findByItemName(itemName);
        for (int i = 0; i < amountList.toArray().length; i++) {
            if (amountList.get(i) == 0)
                continue;
            orderCartService.addCart(userId, itemName, amountList.get(i), sizeList.get(i));
        }
        return "redirect:/";
    }
    //주문 화면
    @GetMapping("/orderPage")
    public String showCart(@SessionAttribute(value = "loginUser", required = false) Long userId, Model model) {
        if (userId == null) {
            return "redirect:/";
        }
        User user = userService.findOne(userId).orElse(null);
        Address address = user.getAddress();
        System.out.println(address.getDetail());
        model.addAttribute("address", address);
        model.addAttribute("loginUser", userId);
        List<CartDto> cartGetItems = cartService.findAllByDto(userId);
        List<CartDto> cartItems = orderCartService.findAllByDto(userId);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartGetItems", cartGetItems);
        return "/order/orderPage";
    }
    @PostMapping("/orderPage/delete")
    public String deleteOrderCart(@RequestParam(value = "id", defaultValue = "0") Long id) {
        orderCartService.delete(id);
        return "redirect:/orderPage";
    }
    @GetMapping("/orderPage/order")
    public String order(@SessionAttribute(value = "loginUser", required = false) Long userId) {
        if (userId == null) {
            return "redirect:/";
        }
        List<OrderCart> cartItems = cartService.findAllByUserId(userId).stream().map(Cart::toOrderCart).toList();
        List<OrderCart> orderCartItems = orderCartService.findAllByUserId(userId);

        orderCartItems.addAll(cartItems);
        if (orderCartItems.size() != 0) {
            orderService.order(userId, orderCartItems);
            orderCartService.deleteAllByUserId(userId);
            cartService.deleteAllByUserId(userId);
        }
        return "redirect:/";
    }
    @PostMapping("/editAddress")
    public String editAddress(@SessionAttribute(value = "loginUser", required = false) Long userId, @ModelAttribute Address address) {
        if (userId == null) {
            return "redirect:/";
        }
        User user = userService.findOne(userId).orElse(null);
        user.makeAddress(new Address(address.getZipCode(), address.getStreet(), address.getDetail()));
        userService.addUser(user);
        return "redirect:/orderPage";
    }
    @GetMapping("/orderInfo")
    public String orderByUser(@SessionAttribute(value = "loginUser", required = false) Long userId, Model model) {
        if (userId == null) {
            return "redirect:/";
        }
        List<OrderDto> orders = orderService.findOrderByUserId(userId).stream().map(Order::toOrderDto).collect(Collectors.toList());
        model.addAttribute("orders", orders);
        model.addAttribute("loginUser", userId);
        return "order/orderInfo";
    }
    @PostMapping("/orderInfo/delete")
    public String orderDelete(@RequestParam(value = "id", defaultValue = "0") Long id) {
                orderService.cancelOrder(id);
        return "redirect:/orderInfo";
    }
    @GetMapping("/admin/orders")
    public String allOrders(Model model, @RequestParam(value = "sort", defaultValue = "0") String sortBy) {
        List<OrderDto> orders;
        if (sortBy.equals("ii")) {
            orders = new ArrayList<>(orderService.findAll().stream().sorted(Comparator.comparing(o -> o.getUser().getUserName())).map(Order::toOrderDto).collect(Collectors.toList()));
        }
        else if (sortBy.equals("id")) {
            List<OrderDto> orderList = new ArrayList<>(orderService.findAll().stream().sorted(Comparator.comparing(o -> o.getUser().getUserName())).map(Order::toOrderDto).collect(Collectors.toList()));
            Collections.reverse(orderList);
            orders = new ArrayList<>(orderList);
        }
        else if (sortBy.equals("pi")) {
            orders = new ArrayList<>(orderService.findAll().stream().sorted(Comparator.comparing(Order::getOrderPrice)).map(Order::toOrderDto).toList());
        }
        else if (sortBy.equals("pd")) {
            List<OrderDto> orderList = new ArrayList<>(orderService.findAll().stream().sorted(Comparator.comparing(Order::getOrderPrice)).map(Order::toOrderDto).toList());
            Collections.reverse(orderList);
            orders = new ArrayList<>(orderList);
        }
        else {
            orders = orderService.findAll().stream().map(Order::toOrderDto).collect(Collectors.toList());
        }
        model.addAttribute("orders", orders);
        return "/admin/order/orders";
    }
    @PostMapping("/admin/orders/delete")
    public String deleteOrder(@RequestParam(value = "id", defaultValue = "0") Long id) {
        orderService.deleteOrder(id);
        return "redirect:/admin/orders";
    }
}
