package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
        //item == clazz
        //item == subItem isAssignableFrom을 쓰면 자식 클래스까지 다 커버가 됨
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;
        if (!StringUtils.hasText(item.getItemName())) {
            errors.rejectValue("itemName", "required");
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            errors.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
            // errors.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "상품가격은 1000원 이상 1000000원 이하입니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            // errors.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 9999개까지 등록가능합니다."));
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        //복합적 검증
        if(item.getPrice() != null && item.getItemName() != null) {
            if (item.getPrice() * item.getQuantity() < 10000) {
                errors.reject("totalPriceMin", new Object[]{10000, item.getPrice() * item.getQuantity()}, null);
                // errors.addError(new ObjectError("item", "가격 * 수량은 10000원 이상이어야 합니다. 현재값은 " + item.getQuantity() * item.getPrice() + "원 입니다."));
            }
        }
    }
}
