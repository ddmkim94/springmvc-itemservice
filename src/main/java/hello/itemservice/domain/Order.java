package hello.itemservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Table(name = "ORDERS")
@Getter
public class Order {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Setter
    @Enumerated(STRING)
    private OrderStatus orderStatus;

    // 주문 날짜 & 주문 상태 지정
    public void addDateAndStatus(LocalDateTime orderDate, OrderStatus orderStatus) {
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
    }
    /**
     * 연관관계 메서드
     */
    public void addMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.addOrder(this);
    }

    public void addDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.addOrder(this);
    }

    /**
     * 정적 팩토리 메서드 -> 객체 생성 역할을 하는 클래스 메서드
     */
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.addMember(member);
        order.addDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.addDateAndStatus(LocalDateTime.now(), OrderStatus.ORDER);
        return order;
    }

    /**
     * 비즈니스 로직
     * 1. 주문취소
     * 2. 전체 주문 가격 조회
     */
    public void cancel() {
        if (delivery.getDeliveryStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소할 수 없습니다.");
        }

        this.setOrderStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : this.getOrderItems()) {
            orderItem.cancel();
        }
    }

    public int getTotalPrice() {
        int totalPrice = 0; // 전체 주문 가격
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
