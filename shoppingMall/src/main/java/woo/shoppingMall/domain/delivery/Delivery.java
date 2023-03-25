package woo.shoppingMall.domain.delivery;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import woo.shoppingMall.domain.Address;
import woo.shoppingMall.domain.BaseTimeEntity;
import woo.shoppingMall.domain.order.Order;

@Entity
@Getter
@NoArgsConstructor
public class Delivery extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Embedded
    private Address address;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    public void makeOrder(Order order) {
        this.order = order;
    }
    public void makeAddress(Address address) {
        this.address = address;
    }
    public void makeDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
