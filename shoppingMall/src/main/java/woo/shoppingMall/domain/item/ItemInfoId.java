package woo.shoppingMall.domain.item;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfoId implements Serializable {
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "size")
    private int size;

    public void makeItemInfoId(Long itemId, int size) {
        this.itemId = itemId;
        this.size = size;
    }
}
