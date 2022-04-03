package hello.itemservice.domain.item;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Book")
@Getter
@NoArgsConstructor
public class Book extends Item{

    private String author;
    private String isbn;

    public Book(String name, int price, int stockQuantity, String author, String isbn) {
        super(name, price, stockQuantity); // Item 클래스의 생성자 호출!!
        this.author = author;
        this.isbn = isbn;
    }
}
