package woo.shoppingMall.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import woo.shoppingMall.domain.item.Item;
import woo.shoppingMall.domain.order.Order;
import woo.shoppingMall.domain.user.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private String nickName;

    private String userName;

    private int grade;

    private String text;

    public Review(User user, Item item, String userName, String nickName, int grade, String text) {
        this.nickName = nickName;
        this.user = user;
        this.item = item;
        this.userName = userName;
        this.grade = grade;
        this.text = text;
    }
    public void makeReview(int grade, String text) {
        this.grade = grade;
        this.text = text;
    }
    public void makeName(String userName, String nickName) {
        this.userName = userName;
        this.nickName = nickName;
    }
}
