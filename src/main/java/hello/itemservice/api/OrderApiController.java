package hello.itemservice.api;

import hello.itemservice.domain.*;
import hello.itemservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 컬렉션이 포함된 주문 조회 컨트롤러 (@OneToMany)
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    /**
     * V2 - 엔티티를 DTO로 변환해서 반환!
     */
    @GetMapping("/api/v2/orders")
    public Result<List<OrderDTO>> ordersV2() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        List<OrderDTO> result = new ArrayList<>();

        for (Order order : all) {
            result.add(new OrderDTO(order));
        }

        return new Result<>(result);
    }

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                orderItem.getItem().getName();
            }
        }
        return all;
    }

    /**
     * DTO로 변환해서 반환하는 말은 엔티티에 대한 의존을 완전히 끊으라는 말!!
     * -> DTO안에 엔티티가 들어가면 안됨, 단 Address와 같은 value object는 상관 X
     */
    @Data
    static class OrderDTO {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDTO> orderItems;

        public OrderDTO(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // 초기화!
            orderDate = order.getOrderDate();
            orderStatus = order.getOrderStatus();
            address = order.getDelivery().getAddress(); // 초기화!
            orderItems = new ArrayList<>();
            for (OrderItem orderItem : order.getOrderItems()) {
                orderItems.add(new OrderItemDTO(orderItem));
            }
        }
    }

    @Data
    private static class OrderItemDTO {
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDTO(OrderItem orderItem) {
            itemName = orderItem.getItem().getName(); // 초기화!
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}