package woo.shoppingMall.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import woo.shoppingMall.domain.Cart;
import woo.shoppingMall.domain.item.Item;
import woo.shoppingMall.service.CartService;
import woo.shoppingMall.service.ItemService;
import woo.shoppingMall.web.dto.CartDto;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final ItemService itemService;
    private final CartService cartService;

    //장바구니에 주문한 상품 담기
    @PostMapping("/cart")
    public String cart(@RequestParam(value = "amount[]") List<Integer> amountList, @RequestParam(value = "size[]") List<Integer> sizeList, @RequestParam(value = "name") String itemName, @SessionAttribute(value = "loginUser", required = false) Long userId) {
        if (userId == null) {
            return "redirect:/";
        }
        Item item = itemService.findByItemName(itemName);
        for (int i = 0; i < amountList.size(); i++) {
            if (amountList.get(i) == 0)
                continue;
            cartService.addCart(userId, itemName, amountList.get(i), sizeList.get(i));
        }
        return "/cart/cart";
    }

    //장바구니 목록 조회
    @GetMapping("/cart")
    public String showCart(@SessionAttribute(value = "loginUser", required = false) Long userId, Model model) {
        if (userId == null) {
            return "redirect:/";
        }
        model.addAttribute("loginUser", userId);
        List<CartDto> cartItems = cartService.findAllByDto(userId);
        model.addAttribute("cartItems", cartItems);
        return "/cart/cart";
    }

    //장바구니 페이지에서 장바구니에 담은 상품 삭제
    @PostMapping("/cart/delete")
    public String deleteOne(@RequestParam(value = "id", defaultValue = "0") Long id) {
        cartService.deleteCart(id);
        return "redirect:/cart";
    }
    //주문 페이지에서 장바구니에 담은 상품 삭제
    @PostMapping("/cart/deleteCart")
    public String deleteCart(@RequestParam(value = "id", defaultValue = "0") Long id) {
        cartService.deleteCart(id);
        return "redirect:/orderPage";
    }
}