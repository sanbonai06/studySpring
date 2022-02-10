package hello.itemservice.domain.item;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Data는 도메인 객체에 사용하기엔 굉장히 위험하므로 @Getter @Setter를 사용하자
 * DTO에서는 사용해도 괜찮지만 그래도 상황을 보고 쓰자
 */

//@Data
@Getter
@Setter
public class Item {

    private Long itemId;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
