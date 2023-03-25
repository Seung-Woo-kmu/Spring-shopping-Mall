package woo.shoppingMall.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import woo.shoppingMall.domain.Review;
import woo.shoppingMall.domain.item.*;
import woo.shoppingMall.domain.order.Order;
import woo.shoppingMall.domain.user.User;
import woo.shoppingMall.service.*;
import woo.shoppingMall.web.dto.FileDto;
import woo.shoppingMall.web.dto.ItemEditDto;
import woo.shoppingMall.web.dto.ItemShowDto;
import woo.shoppingMall.web.validator.ItemValidator;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    private final UserService userService;

    private final OrderService orderService;

    private final ReviewService reviewService;

    private final ItemInfoService itemInfoService;

    private final ItemValidator itemValidator;

    //상품 유효성 검증기 추가 - 새로운 상품 등록 시 이미 존재하는 상품과 이름이 같으면 상품 생성 불가
    @InitBinder("itemEditDto")
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(itemValidator);
    }

    //이미지 파일 저장 폴더
    @Value("${file.dir}")
    private String fileDir;

    //전체 상품목록 리턴
    @GetMapping("/itemList")
    public String itemList(@SessionAttribute(value = "adminUser", required = false) Long adminId, @SessionAttribute(value = "loginUser", required = false) Long userId, Model model) {
        model.addAttribute("loginUser", userId);
        model.addAttribute("adminUser", adminId);
        List<ItemShowDto> items = new ArrayList<>();
        findItems(items);
        model.addAttribute("items", items);
        return "item/itemList";
    }

    //쿼리에 따른 상품 검색 결과 리턴
    @GetMapping("items")
    public String items(@SessionAttribute(value = "adminUser", required = false) Long adminId, @SessionAttribute(value = "loginUser", required = false) Long userId, @RequestParam(value = "q", defaultValue = "전체검색") String query, Model model) {
        model.addAttribute("loginUser", userId);
        model.addAttribute("adminUser", adminId);
        List<ItemShowDto> items = new ArrayList<>();
        if (query.equals("전체검색")) {
            findItems(items);
        } else {
            findItems(items, query);
        }
        model.addAttribute("items", items);
        model.addAttribute("query", query);
        return "item/itemList";
    }
    //상품 아이디에 따른 상품 상세 정보 리턴
    @GetMapping("items/{id}")
    public String itemInfo(@SessionAttribute(value = "adminUser", required = false) Long adminId, @SessionAttribute(value = "loginUser", required = false) Long userId, @PathVariable("id") Long id, Model model, @RequestParam(value = "sort", defaultValue = "0") String sortBy) {
        model.addAttribute("adminUser", adminId);
        model.addAttribute("loginUser", userId);
        Item item = itemService.findOne(id).orElse(null);
        List<Review> reviews;
        //리뷰를 평점에 따라 내림차순으로 정렬
        if (sortBy.equals("gradeD")) {
            reviews = reviewService.findByGrade(item);
        }
        //리뷰를 평점에 따라 오름차순으로 정렬
        else if (sortBy.equals("gradeI")) {
            List<Review> reviewList = reviewService.findByGrade(item);
            Collections.reverse(reviewList);
            reviews = new ArrayList<>(reviewList);
        }
        //리뷰를 생성 시간 혹은 수정 시간에 따라 내림차순으로 정렬
        else if (sortBy.equals("newD")) {
            reviews = reviewService.findByNew(item);
        }
        //리뷰를 생성 시간 혹은 수정 시간에 따라 오름차순으로 정렬
        else if (sortBy.equals("newI")) {
            List<Review> reviewList = reviewService.findByNew(item);
            Collections.reverse(reviewList);
            reviews = new ArrayList<>(reviewList);
        }
        //기본으로 리뷰를 생성 시간 혹은 수정 시간에 따라 내림차순으로 정렬
        else {
            reviews = reviewService.findByNew(item);
        }
        //해당 상품에 대한 전체 회원 리뷰의 평균 평점 계산
        int reviewCount = reviews.size();
        int reviewAverage = 0;
        for (Review review : reviews) {
            reviewAverage += review.getGrade();
        }
        if (reviewAverage != 0) {
            reviewAverage /= reviewCount;
        }
        model.addAttribute("reviewAverage", reviewAverage);
        model.addAttribute("reviewCount", reviewCount);
        //해당 회원이 리뷰를 썼는지 확인
        if (userId != null) {
            User user = userService.findOne(userId).orElse(null);
            Boolean ifWritten = reviewService.ifFindByItemAndUser(item, user);
            model.addAttribute("ifWritten", ifWritten);
            model.addAttribute("nickName", user.getNickName());
        }
        model.addAttribute("reviews", reviews);
        //해당 회원이 해당 상품을 주문 했는지 확인 -> 상품을 주문한 회원만이 리뷰를 쓸 수 있고 총 한 개의 리뷰를 달 수 있다.
        List<Order> orders = orderService.findOrderByUserId(userId);
        for (Order order : orders) {
            Optional<OrderItem> orderItem = order.getOrderItems().stream().filter(oi -> oi.getItemInfo().getItem().equals(item)).findFirst();
            if (orderItem.isPresent()) {
                model.addAttribute("orderItem", orderItem.get().getItemInfo().getItem().getId());
                break;
            }
        }
        List<ItemInfo> items = itemInfoService.findAllByItemId(item.getId());
        for (ItemInfo itemInfo : items) {
            itemInfo.getItemInfoId();
        }

        model.addAttribute("item", item);
        model.addAttribute("items", items);
        return "item/itemInfo";
    }
    @PostMapping("items/{id}")
    public String items(@RequestParam("id") Long reviewId, @RequestParam("grade") int grade, @RequestParam("text") String text, @SessionAttribute(value = "adminUser", required = false) Long adminId, @SessionAttribute(value = "loginUser", required = false) Long userId, @PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("adminUser", adminId);
        model.addAttribute("loginUser", userId);
        Item item = itemService.findOne(id).orElse(null);
        //리뷰 수정 혹은 신규 리뷰 생성 시 화면에 반영
        List<Review> reviews = reviewService.findByItem(item);
        if (userId != null) {
            User user = userService.findOne(userId).orElse(null);
            if (reviewId != 0) {
                Review updateReview = reviewService.findByReviewId(reviewId);
                updateReview.makeReview(grade, text);
                reviewService.add(updateReview);
            }
            else {
                Review createdReview = new Review(user, item, user.getUserName(), user.getNickName(), grade, text);
                reviewService.add(createdReview);
            }
            redirectAttributes.addAttribute("status", true);
            Boolean ifWritten = reviewService.ifFindByItemAndUser(item, user);
            model.addAttribute("ifWritten", ifWritten);
        }
        model.addAttribute("reviews", reviews);
        List<Order> orders = orderService.findOrderByUserId(userId);
        for (Order order : orders) {
            Optional<OrderItem> orderItem = order.getOrderItems().stream().filter(oi -> oi.getItemInfo().getItem().equals(item)).findFirst();
            if (orderItem.isPresent()) {
                model.addAttribute("orderItem", orderItem.get().getItemInfo().getItem().getId());
                break;
            }
        }
        List<ItemInfo> items = itemInfoService.findAllByItemId(item.getId());
        for (ItemInfo itemInfo : items) {
            itemInfo.getItemInfoId();
        }

        model.addAttribute("item", item);
        model.addAttribute("items", items);
        return "redirect:/items/{id}";
    }
    //해당 상품에 대한 해당 회원의 리뷰 삭제
    @GetMapping("/items/review/delete")
    public String deleteReview(@RequestParam(value = "reviewId", defaultValue = "0") Long reviewId) {
        Review review = reviewService.findByReviewId(reviewId);
        Long id = review.getItem().getId();
        reviewService.deleteById(reviewId);
        return "redirect:/items/" + id;
    }
    //관리자 권한으로 해당 회원의 리뷰 삭제
    @PostMapping("/items/review/delete")
    public String adminDeleteReview(@RequestParam(value = "reviewId", defaultValue = "0") Long reviewId) {
        Review review = reviewService.findByReviewId(reviewId);
        Long id = review.getItem().getId();
        reviewService.deleteById(reviewId);
        return "redirect:/admin/reviews";
    }
    //관리자 화면에 상품 목록 리턴
    @GetMapping("/admin/items")
    public String allItem(Model model, @RequestParam(value = "sort", defaultValue = "0") String sortBy) {
        List<ItemInfo> itemInfos;
        //상품 목록을 상품 이름에 대해 오름차순으로 정렬
        if (sortBy.equals("ii")){
            itemInfos = new ArrayList<>(itemInfoService.findItems().stream().sorted(Comparator.comparing(i -> i.getItem().getItemName())).toList());
        }
        //상품 목록을 상품 이름에 대해 내림차순으로 정렬
        else if (sortBy.equals("id")){
            List<ItemInfo> itemInfoList = new ArrayList<>(itemInfoService.findItems().stream().sorted(Comparator.comparing(i -> i.getItem().getItemName())).toList());
            Collections.reverse(itemInfoList);
            itemInfos = new ArrayList<>(itemInfoList);
        }
        //상품 목록을 상품 가격에 대해 오름차순으로 정렬
        else if (sortBy.equals("pi")) {
            itemInfos = new ArrayList<>(itemInfoService.findItems().stream().sorted(Comparator.comparing(i -> i.getItem().getPrice())).toList());
        }
        //상품 목록을 상품 가격에 대해 내림차순으로 정렬
        else if (sortBy.equals("pd")) {
            List<ItemInfo> itemInfoList= new ArrayList<>(itemInfoService.findItems().stream().sorted(Comparator.comparing(i -> i.getItem().getPrice())).toList());
            Collections.reverse(itemInfoList);
            itemInfos = new ArrayList<>(itemInfoList);
        }
        //상품 목록을 상품 재고에 대해 오름차순으로 정렬
        else if (sortBy.equals("si")) {
            itemInfos = itemInfoService.findItems().stream().sorted(Comparator.comparing(ItemInfo::getStockQuantity)).toList();
        }
        //상품 목록을 상품 재고에 대해 내림차순으로 정렬
        else if (sortBy.equals("sd")) {
            List<ItemInfo> itemInfoList = new ArrayList<>(itemInfoService.findItems().stream().sorted(Comparator.comparing(ItemInfo::getStockQuantity)).toList());
            Collections.reverse(itemInfoList);
            itemInfos = new ArrayList<>(itemInfoList);
        }
        else {
            itemInfos = itemInfoService.findItems();
        }

        model.addAttribute("itemInfos", itemInfos);
        return "admin/item/items";
    }
    //관리자 권한으로 해당 상품 삭제
    @PostMapping("/admin/item/delete")
    public String deleteItem(@RequestParam(value = "itemId", defaultValue = "0") Long itemId, @RequestParam(value = "size", defaultValue = "0") int size) throws IOException {
        ItemInfo itemInfo = itemInfoService.findById(new ItemInfoId(itemId, size));
        List<OrderItem> orderItems = orderService.findOrderItemByItemInfo(itemInfo);
        for (OrderItem orderItem : orderItems) {
            orderService.deleteOrderItem(orderItem);
        }
        ItemInfoId itemInfoId = new ItemInfoId(itemId, size);
        itemInfoService.delete(itemInfoId);
        if (itemInfoService.findAllByItemId(itemId).size() == 0) {
            reviewService.deleteByItemId(itemId);
            itemService.delete(itemId);
        }
        return "redirect:/admin/items";
    }
    //관리자 상품 수정 페이지
    @GetMapping("/admin/item/edit")
    public String editItem(@RequestParam(value = "itemId", defaultValue = "0") Long itemId, @RequestParam(value = "size", defaultValue = "0") int size, Model model) {
        ItemInfoId itemInfoId = new ItemInfoId(itemId, size);
        ItemInfo itemInfo = itemInfoService.findById(itemInfoId);
        Item foundItem = itemService.findOne(itemId).orElse(null);
        ItemEditDto itemEditDto = new ItemEditDto(foundItem.getFilePath(), foundItem.getId(), foundItem.getItemName(), foundItem.getItemDetail(), itemInfoId.getSize(), foundItem.getPrice(), itemInfo.getStockQuantity(), foundItem.getItemCategory());
        int itemSize = itemInfoId.getSize();
        FileDto imageFile = new FileDto();
        model.addAttribute("itemEditDto", itemEditDto);
        model.addAttribute("itemSize", itemSize);
        model.addAttribute("imageFile", imageFile);
        return "admin/item/edit";
    }
    //관리자 권한으로 상품 수정 -> 다른 상품의 이름과 겹치면 상품 수정 불가
    @PostMapping("/admin/item/edit")
    public String editingItem(@Validated @ModelAttribute("itemEditDto") ItemEditDto itemEditDto, BindingResult bindingResult, @ModelAttribute("itemSize") int itemSize, @ModelAttribute("imageFile") MultipartFile imageFile , RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            return "admin/item/edit";
        }
        else {
            ItemInfo orderItemInfo = itemInfoService.findById(new ItemInfoId(itemEditDto.getId(), itemSize));
            List<OrderItem> orderItems = orderService.findOrderItemByItemInfo(orderItemInfo);
            //이미지 파일이 비어있으면 기존에 있던 이미지 사용
            if (imageFile.isEmpty()) {
                Item updateItem = new Item(itemEditDto, itemEditDto.getFilePath());
                itemService.addItem(updateItem);
                ItemInfoId itemInfoId = new ItemInfoId(itemEditDto.getId(), itemEditDto.getSize());
                ItemInfo itemInfo = new ItemInfo(updateItem, itemInfoId, itemEditDto.getStockQuantity());
                itemInfoService.add(itemInfo);
                if (itemSize != itemEditDto.getSize()) {
                    for (OrderItem orderItem : orderItems) {
                        orderItem.updateOrderItem(itemInfo);
                        orderService.addOrderItem(orderItem);
                    }
                    itemInfoService.delete(new ItemInfoId(itemEditDto.getId(), itemSize));
                }
                //상품 수정에 성공 시 수정이 완료됐다는 표시를 위해 상태 정보를 넘김
                redirectAttributes.addAttribute("status", true);
                return "redirect:/admin/item/edit?itemId=" + itemEditDto.getId() + "&size=" + itemEditDto.getSize();
            }
            //이미지 파일이 존재하면 새로운 이미지 파일로 바꿈
            String filePath = fileDir + imageFile.getOriginalFilename();
            File file = new File(filePath);
            imageFile.transferTo(file);
            Item updateItem = new Item(itemEditDto, "/shoes/" + imageFile.getOriginalFilename());
            itemService.addItem(updateItem);
            ItemInfoId itemInfoId = new ItemInfoId(itemEditDto.getId(), itemEditDto.getSize());
            ItemInfo itemInfo = new ItemInfo(updateItem, itemInfoId, itemEditDto.getStockQuantity());
            itemInfoService.add(itemInfo);
            if (itemSize != itemEditDto.getSize()) {
                for (OrderItem orderItem : orderItems) {
                    orderItem.updateOrderItem(itemInfo);
                    orderService.addOrderItem(orderItem);
                }
                itemInfoService.delete(new ItemInfoId(itemEditDto.getId(), itemSize));
            }
            //상품 수정에 성공 시 수정이 완료됐다는 표시를 위해 상태 정보를 넘김
            redirectAttributes.addAttribute("status", true);

            return "redirect:/admin/item/edit?itemId=" + updateItem.getId() + "&size=" + itemInfoId.getSize();
        }
    }
    @GetMapping("/admin/item/add")
    public String addItem(@ModelAttribute("addItem") ItemEditDto addItem, @ModelAttribute("imageFile") MultipartFile imageFile) {
        return "/admin/item/add";
    }
    //관리자 권한으로 새로운 상품 추가 -> 다른 상품의 이름과 겹치면 상품 추가 불가
    @PostMapping("/admin/item/add")
    public String addedItem(@ModelAttribute("addItem") ItemEditDto addItem, BindingResult bindingResult, @ModelAttribute("imageFile") MultipartFile imageFile, RedirectAttributes redirectAttributes, Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            return "/admin/item/add";
        }
        if (imageFile.isEmpty()) {
            model.addAttribute("status", false);
            return "/admin/item/add";
        }
        //새로운 상품 추가
        Item foundItem = itemService.findByItemName(addItem.getItemName());
        if (foundItem == null) {
            String filePath = fileDir + imageFile.getOriginalFilename();
            File file = new File(filePath);
            imageFile.transferTo(file);
            Item item = new Item(addItem.getItemName(), addItem.getItemDetail(), addItem.getPrice(), addItem.getItemCategory(), "/shoes/" + imageFile.getOriginalFilename());
            itemService.addItem(item);
            ItemInfoId itemInfoId = new ItemInfoId(item.getId(), addItem.getSize());
            ItemInfo itemInfo = new ItemInfo(item, itemInfoId, addItem.getStockQuantity());
            itemInfoService.add(itemInfo);
            redirectAttributes.addAttribute("status", true);

            return "redirect:/admin/items";
        }
        ItemInfoId itemInfoId = new ItemInfoId(foundItem.getId(), addItem.getSize());
        ItemInfo itemInfo = new ItemInfo(foundItem, itemInfoId, addItem.getStockQuantity());
        itemInfoService.add(itemInfo);
        redirectAttributes.addAttribute("status", true);
        return "redirect:/admin/items";
    }
    //전체 리뷰 목록 보기
    @GetMapping("/admin/reviews")
    public String review(Model model, @RequestParam(value = "sort", defaultValue = "0") String sortBy) {
        List<Review> reviews;
        //리뷰 목록을 회원들의 이름에 대해 오름차순으로 정렬
        if (sortBy.equals("ii")) {
            reviews = new ArrayList<>(reviewService.findAll().stream().sorted(Comparator.comparing(Review::getUserName)).toList());
        }
        //리뷰 목록을 회원들의 이름에 대해 내림차순으로 정렬
        else if (sortBy.equals("id")) {
            List<Review> reviewList = new ArrayList<>(reviewService.findAll().stream().sorted(Comparator.comparing(Review::getUserName)).toList());
            Collections.reverse(reviewList);
            reviews = new ArrayList<>(reviewList);
        }
        //리뷰 목록을 회원들의 평점에 대해 오름차순으로 정렬
        else if (sortBy.equals("gi")) {
            reviews = new ArrayList<>(reviewService.findAll().stream().sorted(Comparator.comparing(Review::getGrade)).toList());
        }
        //리뷰 목록을 회원들의 평점에 대해 내림차순으로 정렬
        else if (sortBy.equals("gd")) {
            List<Review> reviewList = new ArrayList<>(reviewService.findAll().stream().sorted(Comparator.comparing(Review::getGrade)).toList());
            Collections.reverse(reviewList);
            reviews = new ArrayList<>(reviewList);
        }
        else {
            reviews = reviewService.findAll();
        }
        model.addAttribute("reviews", reviews);
        return "/admin/review/reviews";
    }
    //상품에 대한 리뷰들과 평점을 포함한 상품 목록 보기
    private void findItems(List<ItemShowDto> items) {
        List<Item> itemList = itemService.findItems();
        for (Item item : itemList) {
            List<Review> reviews = reviewService.findByItem(item);
            int grade = 0;
            for (Review review : reviews) {
                grade += review.getGrade();
            }
            if (grade != 0) {
                grade /= reviews.size();
            }
            items.add(new ItemShowDto(item, reviews.size(), grade));
        }
    }
    //상품에 대한 검색 결과를 토대로 상품에 대한 리뷰들과 평점을 포함한 상품 목록 보기
    private void findItems(List<ItemShowDto> items, String query) {
        List<Item> itemList = itemService.findItems(query);
        for (Item item : itemList) {
            List<Review> reviews = reviewService.findByItem(item);
            int grade = 0;
            for (Review review : reviews) {
                grade += review.getGrade();
            }
            if (grade != 0) {
                grade /= reviews.size();
            }
            items.add(new ItemShowDto(item, reviews.size(), grade));
        }
    }
}