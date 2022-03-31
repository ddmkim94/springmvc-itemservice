package hello.itemservice.domain;

import hello.itemservice.domain.item.Item;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
public class ChildCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "child_category_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_category_id")
    private ParentCategory parentCategory;

    private String name;
}
