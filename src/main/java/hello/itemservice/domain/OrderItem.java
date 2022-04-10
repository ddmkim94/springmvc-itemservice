package hello.itemservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hello.itemservice.domain.item.Item;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
public class OrderItem {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    public void addOrderItem(Item item, int price, int count) {
        this.item = item;
        this.orderPrice = price;
        this.count = count;
    }

    /**
     * 정적 팩토리 메서드 -> 객체 생성 메서드
     */
    public static OrderItem createOrderItem(Item item, int price, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.addOrderItem(item, price, count);

        item.removeStock(count);
        return orderItem;
    }

    public void addOrder(Order order) {
        this.order = order;
    }

    /**
     * Getter 사용 이유? <br>
     * 필드를 사용해도 상관없지만, JPA 프록시를 다루는 경우에는 달라짐 <br>
     * 만약 조회한 엔티티가 프록시 객체라면 필드에 직접 접근을 하게되면 원본 객체를 가져오지 못함 <br>
     * 이 문제는 equals(), hashCode()를 JPA 프록시 객체로 구현할 때 문제가 될 수 있다. <br>
     * 프록시 객체의 equals()를 호출했는데 필드에 직접 접근하면 프록시 객체는 값이 없는 상태기 떄문에 <br>
     * 항상 null이 반환되기 때문에, equals, hashCode를 구현하는 경우에는
     * 반드시 내부에서 getter를 사용해서 구현!!
     *
     */
    public int getTotalPrice() {
        return this.getOrderPrice() * this.getCount();
    }

    public void cancel() {
        this.getItem().addStock(getCount());
    }
}
