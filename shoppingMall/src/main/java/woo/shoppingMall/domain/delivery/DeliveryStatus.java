package woo.shoppingMall.domain.delivery;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
    CANCEL("배달 취소"), READY("배달 준비 중"), DELIVER("배달 중"), COMPLETE("배달 완료");

    private String status;
    DeliveryStatus(String deliveryStatus) {
        this.status = deliveryStatus;
    }
}
