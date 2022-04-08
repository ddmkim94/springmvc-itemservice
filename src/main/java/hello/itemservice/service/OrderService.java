package hello.itemservice.service;

import hello.itemservice.domain.*;
import hello.itemservice.domain.item.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.MemberRepository;
import hello.itemservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문 생성!
     */
    @Transactional(readOnly = false)
    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findById(memberId); // 회원 조회
        Item item = itemRepository.findById(itemId); // 아이템 조회

        Delivery delivery = new Delivery(member.getAddress(), DeliveryStatus.READY); // 배송 정보 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count); // 주문 상품 생성

        Order order = Order.createOrder(member, delivery, orderItem);
        orderRepository.save(order);
        return order.getId();
    }

    /**
     * 주문 취소!!
     */
    @Transactional(readOnly = false)
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId);
        order.cancel();
    }

    /**
     * 총 주문 가격 조회
     */
    public int priceOrder(Long orderId) {
        Order order = orderRepository.findById(orderId);
        return order.getTotalPrice();
    }

    /**
     * 주문 검색!!
     */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
