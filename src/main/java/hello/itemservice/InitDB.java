package hello.itemservice;

import hello.itemservice.domain.*;
import hello.itemservice.domain.item.Book;
import hello.itemservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            Member member = createMember("이민경", new Address("부산시", "홍직동", "506호"));
            em.persist(member);

            Book book1 = createBook("JPA BOOK1", 10000, 100, "김영한", "1234");
            em.persist(book1);

            Book book2 = createBook("JPA BOOK2", 20000, 100, "김영한","4567");
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, book2.getPrice(), 2);

            Order order = Order.createOrder(member, createDelivery(member), orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("노휘오", new Address("부산시", "홍직동", "507호"));
            em.persist(member);

            Book book1 = createBook("SPRING BOOK1", 20000, 200,"김영한", "1557");
            em.persist(book1);

            Book book2 = createBook("SPRING BOOK2", 40000, 300, "김영한","2546");
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, book1.getPrice(), 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, book2.getPrice(), 4);

            Order order = Order.createOrder(member, createDelivery(member), orderItem1, orderItem2);
            em.persist(order);
        }

        private Delivery createDelivery(Member member) {
            return new Delivery(member.getAddress(), DeliveryStatus.READY);
        }

        private Member createMember(String name, Address address) {
            return new Member(name, new Address(address.getCity(), address.getStreet(), address.getZipcode()));
        }

        private static Book createBook(String name, int price, int stockQuantity, String author, String isbn) {
            return new Book(name, price, stockQuantity, author, isbn);
        }
    }
}