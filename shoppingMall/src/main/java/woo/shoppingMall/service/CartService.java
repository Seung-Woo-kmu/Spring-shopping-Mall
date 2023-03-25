package woo.shoppingMall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woo.shoppingMall.domain.Cart;
import woo.shoppingMall.domain.item.Item;
import woo.shoppingMall.domain.item.ItemInfo;
import woo.shoppingMall.domain.item.ItemInfoId;
import woo.shoppingMall.domain.user.User;
import woo.shoppingMall.repository.CartRepository;
import woo.shoppingMall.repository.ItemInfoRepository;
import woo.shoppingMall.repository.ItemRepository;
import woo.shoppingMall.repository.UserRepository;
import woo.shoppingMall.web.dto.CartDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final ItemInfoRepository itemInfoRepository;

    private final CartRepository cartRepository;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    //장바구니에 상품 추가
    @Transactional
    public void addCart(Long userId, String itemName, int orderAmount, int size) {
        User user = userRepository.findById(userId).orElse(null);
        Item item = itemRepository.findByItemName(itemName);
        Cart cart = new Cart(user, item, orderAmount, size);
        cartRepository.save(cart);
    }

    //장바구니에서 상품 삭제
    @Transactional
    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }
    public List<CartDto> findAllByDto(Long userId) {
        return cartRepository.findAllByUserId(userId).stream().map(Cart::toEntity).collect(Collectors.toList());
    }
    public List<Cart> findAllByUserId(Long userId){
        return cartRepository.findAllByUserId(userId);
    }
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    //해당 회원의 장바구니 상품 삭제
    @Transactional
    public void deleteAllByUserId(Long userId) {
        cartRepository.deleteAllByUserId(userId);
    }
}
