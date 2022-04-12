package hello.itemservice.api;

import hello.itemservice.domain.*;
import hello.itemservice.repository.OrderRepository;
import hello.itemservice.repository.order.query.OrderQueryDTO;
import hello.itemservice.repository.order.query.OrderQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private final OrderQueryRepository orderQueryRepository;

    /**
     * V4 - JPA에서 DTO 직접 조회
     */
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDTO> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDTO> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDTO> result = new ArrayList<>();

        for (Order order : orders) {
            result.add(new OrderDTO(order));
        }
        return result;
    }

    /**
     * V3 - 엔티티 -> DTO 변환 (fetch join 사용!)
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDTO> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDTO> result = new ArrayList<>();

        for (Order order : orders) {
            result.add(new OrderDTO(order));
        }
        return result;
    }

    /**
     * V2 - 엔티티 -> DTO 변환 (fetch join 사용 X)
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
        private List<OrderItemDTO> orderItems; // OrderItem 엔티티 대신 DTO에 받아서 사용!

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