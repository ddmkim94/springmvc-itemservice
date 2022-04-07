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

    public Book(Long id, String name, int price, int stockQuantity, String author, String isbn) {
        super(id, name, price, stockQuantity);
        this.author = author;
        this.isbn = isbn;
    }

    public Book(String name, int price, int stockQuantity, String author, String isbn) {
        super(name, price, stockQuantity); // Item 클래스의 생성자 호출!!
        this.author = author;
        this.isbn = isbn;
    }

    // 변경 메서드
    public void changeBook(String name, int price, int stockQuantity, String author, String isbn) {
        super.changeItem(name, price, stockQuantity);
        this.author = author;
        this.isbn = isbn;
    }
}
