package woo.shoppingMall.web.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import woo.shoppingMall.domain.item.Item;
import woo.shoppingMall.repository.ItemRepository;
import woo.shoppingMall.web.dto.ItemEditDto;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ItemValidator implements Validator {
    private final ItemRepository itemRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return ItemEditDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ItemEditDto itemEditDto = (ItemEditDto) target;
        Optional<Item> item = Optional.ofNullable(itemRepository.findByItemName(itemEditDto.getItemName()));
        Optional<Item> item2 = item.filter(i -> i.getId().equals(itemEditDto.getId()));
        System.out.println(item);
        System.out.println(item2);
        if (item.isPresent() && item2.isEmpty()) {
            errors.rejectValue("itemName", "itemNameError", "이미 존재하는 다른 상품의 이름 입니다.");
        }
    }
}
