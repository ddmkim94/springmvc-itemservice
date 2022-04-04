package hello.itemservice.service;

import hello.itemservice.domain.*;
import hello.itemservice.domain.item.Book;
import hello.itemservice.domain.item.Item;
import hello.itemservice.exception.NotEnoughStockException;
import hello.itemservice.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired MemberService memberService;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    @Autowired ItemService itemService;

    @Test
    @DisplayName("상품 주문 테스트")
    void order() throws Exception {

        // 회원 정보 생성
        Address address = new Address("서울 동대문구", "망우로20길86", "02514");
        Member member = new Member("박은빈", address);
        Long memberId = memberService.join(member);

        // 상품 정보 생성
        Book onePiece = new Book("원피스 100", 4500, 100, "오다 에이치로", "1557");
        Book conan = new Book("명탐정 코난 101", 3500, 200, "고쇼 아오이", "8962");
        itemService.saveItem(onePiece);
        itemService.saveItem(conan);

        // 상품 주문 정보 생성
        Long order = orderService.order(memberId, onePiece.getId(), 50);
        Order findOrder = orderRepository.findById(order);

        assertThat(order).isEqualTo(findOrder.getId());
        assertThat(onePiece.getStockQuantity()).isEqualTo(50);
        assertThat(findOrder.getTotalPrice()).isEqualTo(225000);
    }

    @Test
    @DisplayName("상품주문 재고수량초과 테스트")
    public void overQuantityTest() throws Exception {
        // 회원 정보 생성
        Address address = new Address("서울 동대문구", "망우로20길86", "02514");
        Member member = new Member("박은빈", address);
        Long memberId = memberService.join(member);

        // 상품 정보 생성
        Book onePiece = new Book("원피스 100", 4500, 100, "오다 에이치로", "1557");
        itemService.saveItem(onePiece);

        assertThrows(NotEnoughStockException.class, () -> orderService.order(memberId, onePiece.getId(), 101));
    }

    @Test
    @DisplayName("주문 취소 테스트")
    void cancelOrder() throws Exception {
        // 회원 정보 생성
        Address address = new Address("서울 동대문구", "망우로20길86", "02514");
        Member member = new Member("박은빈", address);
        Long memberId = memberService.join(member);

        // 상품 정보 생성
        Book onePiece = new Book("원피스 100", 4500, 100, "오다 에이치로", "1557");
        itemService.saveItem(onePiece);

        Long orderId = orderService.order(memberId, onePiece.getId(), 50); // 50개 주문!!
        orderService.cancelOrder(orderId);

        Order order = orderRepository.findById(orderId);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(onePiece.getStockQuantity()).isEqualTo(100); // 취소 후 재고는 그대로 100개
    }
}