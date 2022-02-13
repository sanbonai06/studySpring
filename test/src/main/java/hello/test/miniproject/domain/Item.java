package hello.test.miniproject.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
public class Item {
    private Long id;
    private String name;
    private Integer price;
    private Integer quantity;

    public Item() {

    }

    public Item(String name, Integer price, Integer quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
