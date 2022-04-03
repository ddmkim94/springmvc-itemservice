package hello.itemservice.domain.item;

import hello.itemservice.domain.ChildCategory;
import hello.itemservice.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter @NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype") // 타입 식별자 컬럼 이름 지정
public abstract class Item {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @OneToMany(mappedBy = "item")
    private List<ChildCategory> childCategories = new ArrayList<>(); // 하위 카테고리를 담을 list

    private String name;
    private int price;
    private int stockQuantity;

    public Item(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // ==비즈니스 로직==//
    /**
     * 재고 증가 -> 재고 증가 or 주문 취소
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * 재고 감소 -> 주문
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity; // 남은 재고 수량
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}