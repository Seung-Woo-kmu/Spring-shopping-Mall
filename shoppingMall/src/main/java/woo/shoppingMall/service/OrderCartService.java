package woo.shoppingMall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woo.shoppingMall.domain.Cart;
import woo.shoppingMall.domain.item.Item;
import woo.shoppingMall.domain.order.OrderCart;
import woo.shoppingMall.domain.user.User;
import woo.shoppingMall.repository.ItemRepository;
import woo.shoppingMall.repository.OrderCartRepository;
import woo.shoppingMall.repository.UserRepository;
import woo.shoppingMall.web.dto.CartDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderCartService {
    //주문 장바구니는 장바구니와 유사하며 상품을 장바구니에 담지 않고 바로 주문 시 주문 장바구니에 담기도록 함
    //주문 장바구니에서 주문 시 주문이 완료됨
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderCartRepository orderCartRepository;

    //주문 장바구니에 상품 추가
    @Transactional
    public void addCart(Long userId, String itemName, int orderAmount, int size) {
        User user = userRepository.findById(userId).orElse(null);
        Item item = itemRepository.findByItemName(itemName);
        OrderCart cart = new OrderCart(user, item, orderAmount, size);
        orderCartRepository.save(cart);
    }

    //주문 장바구니에서 상품 삭제
    @Transactional
    public void delete(Long orderCartId) {
        orderCartRepository.deleteById(orderCartId);
    }
    public List<CartDto> findAllByDto(Long userId) {
        return orderCartRepository.findAllByUserId(userId).stream().map(OrderCart::toEntity).collect(Collectors.toList());
    }

    //해당 회원의 주문 장바구니 상품을 모두 삭제
    @Transactional
    public void deleteAllByUserId(Long userId) {
        orderCartRepository.deleteAllByUserId(userId);
    }
    public List<OrderCart> findAllByUserId(Long userId) {
        return orderCartRepository.findAllByUserId(userId);
    }
}
