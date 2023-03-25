package woo.shoppingMall.domain;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import woo.shoppingMall.domain.item.Item;
import woo.shoppingMall.domain.item.ItemCategory;
import woo.shoppingMall.domain.item.ItemInfo;
import woo.shoppingMall.domain.item.ItemInfoId;
import woo.shoppingMall.domain.order.OrderCart;
import woo.shoppingMall.domain.user.Authorization;
import woo.shoppingMall.domain.user.User;
import woo.shoppingMall.repository.ItemInfoRepository;
import woo.shoppingMall.service.ItemService;
import woo.shoppingMall.service.OrderService;
import woo.shoppingMall.service.ReviewService;
import woo.shoppingMall.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitData {
    private final UserService userService;
    private final ItemService itemService;

    private final OrderService orderService;

    private final ReviewService reviewService;
    private final ItemInfoRepository itemInfoRepository;
    @PostConstruct
    private void init() {
        //회원 생성
        User userA = new User("a","a","a","안녕", "01011111111", "swoo7246@naver.com",new Address("46990", "부산광역시", "105호"), Authorization.NORMAL);
        User userB = new User("b","b","b","마우스" ,"01022222222", "b2342@naver.com",new Address("35803", "서울특별시" ,"90호"), Authorization.NORMAL);
        User userD = new User("d","d","d", "알바생","01022222222", "b2342@naver.com",new Address("35803", "서울특별시" ,"90호"), Authorization.NORMAL);
        User userC = new User("c","c","c", "집밥","01033333333", "c12324@naver.com",new Address("90002", "세종특별자치시" ,"901호"), Authorization.ADMIN);

        userService.addUser(userA);
        userService.addUser(userB);
        userService.addUser(userD);
        userService.addUser(userC);

        //상품 생성
        Item itemA = new Item("데일리 스니커즈 블랙", "데일리로 신기 좋은 착화감이 좋은 운동화입니다.", 30000, ItemCategory.SNEAKERS, "/shoes/blackSneakers.jpg");
        Item itemB = new Item("데일리 힐 슈즈", "펄감이 있는 토 오프 슈즈입니다.", 40000, ItemCategory.HIGH_SHOES, "/shoes/highHeel.jpg");
        Item itemC = new Item("화이트 웨딩 슈즈", "화려한 비즈감으로 웨딩 드레스를 돋보이게 합니다.", 40000, ItemCategory.HIGH_SHOES, "/shoes/shoes.jpg");
        Item itemD = new Item("GAP 가죽 부츠", "일상 생활에 신기 편한 부츠입니다.", 20000, ItemCategory.BOOTS, "/shoes/fallBoots.jpg");
        Item itemE = new Item("스노우힐 방한 부츠", "양털로 발을 따뜻하게 감싸주는 부츠입니다.", 30000, ItemCategory.BOOTS, "/shoes/winterBoots.jpg");
        Item itemF = new Item("스쿠펫 슬리퍼", "발에 휴식을 주는 편한 슬리퍼입니다.", 5000, ItemCategory.SLIPPER, "/shoes/slipper.jpg");
        Item itemG = new Item("데일리 화이트 하이힐", "특별한 날 돋보이고 싶을 때 신는 하이힐입니다.", 10000, ItemCategory.HIGH_SHOES, "/shoes/whiteShoes.jpg");
        Item itemH = new Item("캐주얼 스니커즈", "청바지에 잘 어울리는 스니커즈입니다.", 15000, ItemCategory.SNEAKERS, "/shoes/beautifulSneakers.jpg");
        Item itemI = new Item("에어맥스 스니커즈", "캐쥬얼에 잘 어울리는 스니커즈입니다.", 15400, ItemCategory.SNEAKERS, "/shoes/sneakers.png");
        Item itemJ = new Item("실내 거실화 슬리퍼", "실내에서 신기 편한 슬리퍼입니다.", 6000, ItemCategory.SNEAKERS, "/shoes/whiteSlipper.jpg");
        Item itemK = new Item("산악 스포츠 운동화", "등산할 때 미끄러짐을 방지해주는 산악 전문 운동화입니다.", 15000, ItemCategory.SNEAKERS,  "/shoes/mountainSneakers.jpg");
        Item itemL = new Item("마운틴 가죽 부츠", "등산할 때 발목을 잡아주는 산악 부츠입니다.", 60000, ItemCategory.BOOTS, "/shoes/mountainBoots.jpg");
        Item itemM = new Item("소프트 슬리퍼", "극세사 원단의 가벼운 슬리퍼입니다.", 12000, ItemCategory.SLIPPER, "/shoes/homeSlipper.jpg");
        Item itemN = new Item("경량 운동화", "통기성이 좋은 여름용 워킹화입니다.", 27000, ItemCategory.SNEAKERS, "/shoes/blueSneakers.jpg");

        itemService.addItem(itemA);
        itemService.addItem(itemB);
        itemService.addItem(itemC);
        itemService.addItem(itemD);
        itemService.addItem(itemE);
        itemService.addItem(itemF);
        itemService.addItem(itemG);
        itemService.addItem(itemH);
        itemService.addItem(itemI);
        itemService.addItem(itemJ);
        itemService.addItem(itemK);
        itemService.addItem(itemL);
        itemService.addItem(itemM);
        itemService.addItem(itemN);

        //상품 사이즈와 재고 추가
        ItemInfoId itemInfoIdA = new ItemInfoId(itemA.getId(), 250);
        ItemInfo itemInfoA = new ItemInfo(itemA, itemInfoIdA, 1000);
        ItemInfoId itemInfoIdA1 = new ItemInfoId(itemA.getId(), 270);
        ItemInfo itemInfoA1 = new ItemInfo(itemA, itemInfoIdA1, 500);
        ItemInfoId itemInfoIdA2 = new ItemInfoId(itemA.getId(), 260);
        ItemInfo itemInfoA2 = new ItemInfo(itemA, itemInfoIdA2, 500);

        ItemInfoId itemInfoIdB = new ItemInfoId(itemB.getId(), 250);
        ItemInfo itemInfoB = new ItemInfo(itemB, itemInfoIdB, 10);

        ItemInfoId itemInfoIdB1 = new ItemInfoId(itemB.getId(), 240);
        ItemInfo itemInfoB1 = new ItemInfo(itemB, itemInfoIdB1, 10);

        ItemInfoId itemInfoIdC = new ItemInfoId(itemC.getId(), 250);
        ItemInfo itemInfoC = new ItemInfo(itemC, itemInfoIdC, 190);

        ItemInfoId itemInfoIdD = new ItemInfoId(itemD.getId(), 210);
        ItemInfo itemInfoD = new ItemInfo(itemD, itemInfoIdD, 120);

        ItemInfoId itemInfoIdE = new ItemInfoId(itemE.getId(), 250);
        ItemInfo itemInfoE = new ItemInfo(itemE, itemInfoIdE, 102);

        ItemInfoId itemInfoIdF = new ItemInfoId(itemF.getId(), 250);
        ItemInfo itemInfoF = new ItemInfo(itemF, itemInfoIdF, 1045);

        ItemInfoId itemInfoIdG = new ItemInfoId(itemG.getId(), 200);
        ItemInfo itemInfoG = new ItemInfo(itemG, itemInfoIdG, 1023);

        ItemInfoId itemInfoIdH = new ItemInfoId(itemH.getId(), 230);
        ItemInfo itemInfoH = new ItemInfo(itemH, itemInfoIdH, 1040);

        ItemInfoId itemInfoIdI = new ItemInfoId(itemI.getId(), 230);
        ItemInfo itemInfoI = new ItemInfo(itemI, itemInfoIdI, 1040);

        ItemInfoId itemInfoIdJ = new ItemInfoId(itemJ.getId(), 210);
        ItemInfo itemInfoJ = new ItemInfo(itemJ, itemInfoIdJ, 1040);

        ItemInfoId itemInfoIdK = new ItemInfoId(itemK.getId(), 210);
        ItemInfo itemInfoK = new ItemInfo(itemK, itemInfoIdK, 1040);

        ItemInfoId itemInfoIdL = new ItemInfoId(itemL.getId(), 250);
        ItemInfo itemInfoL = new ItemInfo(itemL, itemInfoIdL, 500);

        ItemInfoId itemInfoIdM = new ItemInfoId(itemM.getId(), 200);
        ItemInfo itemInfoM = new ItemInfo(itemM, itemInfoIdM, 700);

        ItemInfoId itemInfoIdN = new ItemInfoId(itemN.getId(), 220);
        ItemInfo itemInfoN = new ItemInfo(itemN, itemInfoIdN, 800);

        itemInfoRepository.save(itemInfoA);
        itemInfoRepository.save(itemInfoA1);
        itemInfoRepository.save(itemInfoA2);
        itemInfoRepository.save(itemInfoB);
        itemInfoRepository.save(itemInfoB1);
        itemInfoRepository.save(itemInfoC);
        itemInfoRepository.save(itemInfoD);
        itemInfoRepository.save(itemInfoE);
        itemInfoRepository.save(itemInfoF);
        itemInfoRepository.save(itemInfoG);
        itemInfoRepository.save(itemInfoH);
        itemInfoRepository.save(itemInfoI);
        itemInfoRepository.save(itemInfoJ);
        itemInfoRepository.save(itemInfoK);
        itemInfoRepository.save(itemInfoL);
        itemInfoRepository.save(itemInfoM);
        itemInfoRepository.save(itemInfoN);

        //주문 생성
        List<OrderCart> orderCarts = new ArrayList<>();
        orderCarts.add(new OrderCart(userA, itemA, 20, 250));
        orderCarts.add(new OrderCart(userA, itemA, 30, 260));
        orderService.order(1L, orderCarts);

        //리뷰 생성
        reviewService.add(new Review(userA, itemA, userA.getUserName(), userA.getNickName(), 3, "배송이 빨라서 좋았어요"));

        //주문 생성
        List<OrderCart> orderCarts1 = new ArrayList<>();
        orderCarts1.add(new OrderCart(userB, itemA, 30, 260));
        orderService.order(2L, orderCarts1);

        //리뷰 생성
        reviewService.add(new Review(userB, itemA, userB.getUserName(), userB.getNickName(), 5, "너무 좋아요"));

        //주문 생성
        List<OrderCart> orderCarts2 = new ArrayList<>();
        orderCarts2.add(new OrderCart(userA, itemB, 5, 240));
        orderService.order(1L, orderCarts2);

        //리뷰 생성
        reviewService.add(new Review(userA, itemB, userA.getUserName(), userA.getNickName(), 1, "신기 불편합니다."));
    }
}
