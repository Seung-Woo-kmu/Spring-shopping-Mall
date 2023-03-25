package woo.shoppingMall.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import woo.shoppingMall.domain.item.Item;
import woo.shoppingMall.domain.item.ItemCategory;

@Getter
@Setter
@AllArgsConstructor
public class ItemEditDto {
    @NotBlank
    private String filePath;

    @NotNull
    private Long id;

    @NotBlank
    private String itemName;

    @NotBlank
    private String itemDetail;

    @NotNull
    private Integer size;

    @NotNull
    private Integer price;

    @NotNull
    private Integer stockQuantity;

    @NotNull
    private ItemCategory itemCategory;
}
