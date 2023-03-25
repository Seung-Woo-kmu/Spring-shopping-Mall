package woo.shoppingMall.domain.order;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ORDER("주문 완료"), CANCEL("주문 취소");

    private String status;
    OrderStatus(String orderStatus) {
        this.status = orderStatus;
    }
}
