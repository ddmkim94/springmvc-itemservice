package hello.itemservice.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ParentCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parent_category_id")
    private Long id;

    @OneToMany(mappedBy = "parentCategory")
    private List<ChildCategory> childCategories = new ArrayList<>();

    private String name;
}
