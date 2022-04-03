package hello.itemservice.domain;

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

    public int getTotalPrice() {
        return this.getOrderPrice() * this.getCount();
    }

    public void cancel() {
        this.getItem().addStock(getCount());
    }
}
