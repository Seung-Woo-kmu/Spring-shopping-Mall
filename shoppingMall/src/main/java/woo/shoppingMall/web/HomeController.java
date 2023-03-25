package woo.shoppingMall.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import woo.shoppingMall.domain.Review;
import woo.shoppingMall.domain.item.Item;
import woo.shoppingMall.domain.user.User;
import woo.shoppingMall.service.ItemService;
import woo.shoppingMall.service.ReviewService;
import woo.shoppingMall.web.dto.ItemShowDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ItemService itemService;

    private final ReviewService reviewService;

    @GetMapping("/")
    public String home(@SessionAttribute(name = "loginUser", required = false) Long userId, Model model) {
        List<ItemShowDto> items = new ArrayList<>();
        List<Item> itemList = itemService.findItems();
        for (Item item : itemList) {
            List<Review> reviews = reviewService.findByItem(item);
            Integer grade = 0;
            for (Review review : reviews) {
                grade += review.getGrade();
            }
            if (grade != 0) {
                grade /= reviews.size();
            }
            items.add(new ItemShowDto(item, reviews.size(), grade));
        }
        model.addAttribute("items", items);
        if (userId == null) {
            return "home";
        }
        model.addAttribute("loginUser", userId);
        return "home";
    }
    @GetMapping("/admin")
    public String adminHome(@SessionAttribute(name = "adminUser", required = false) Long userId, Model model) {
        List<ItemShowDto> items = new ArrayList<>();
        List<Item> itemList = itemService.findItems();
        for (Item item : itemList) {
            List<Review> reviews = reviewService.findByItem(item);
            Integer grade = 0;
            for (Review review : reviews) {
                grade += review.getGrade();
            }
            if (grade != 0) {
                grade /= reviews.size();
            }
            items.add(new ItemShowDto(item, reviews.size(), grade));
        }

        model.addAttribute("items", items);
        if (userId == null) {
            return "home";
        }
        model.addAttribute("adminUser", userId);
        return "/admin/home";
    }
}
