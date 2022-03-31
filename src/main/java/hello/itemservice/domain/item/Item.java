package hello.itemservice.domain.item;

import hello.itemservice.domain.ChildCategory;
import lombok.Getter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
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
}
