package hello.itemservice.domain;

import lombok.Data;

@Data
public class Item {

    private Long id;
    private String name;
    private int price;
    private int quantity;
}
