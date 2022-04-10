package hello.itemservice.api;

import hello.itemservice.domain.Address;
import hello.itemservice.domain.Order;
import hello.itemservice.domain.OrderSearch;
import hello.itemservice.domain.OrderStatus;
import hello.itemservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    /**
     * fetch join 최적화! -> 쿼리 1개 실행!
     */
    @GetMapping("/api/v3/simple-orders")
    public Yeonseo<List<SimpleOrderDTO>> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDTO> result = new ArrayList<>();

        for (Order order : orders) {
            SimpleOrderDTO dto = new SimpleOrderDTO(order);
            result.add(dto);
        }

        return new Yeonseo<>(result);
    }

    @GetMapping("/api/v2/simple-orders")
    public Yeonseo<List<SimpleOrderDTO>> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDTO> result = new ArrayList<>();

        for (Order order : orders) {
            result.add(new SimpleOrderDTO(order));
        }
        return new Yeonseo<>(result);
    }

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getName(); // LAZY 강제 초기화
            order.getDelivery().getAddress(); // LAZY 강제 초기화
        }
        return orders;
    }

    @Data
    @AllArgsConstructor
    static class Yeonseo<T> {
        private T data;
    }


    // 주문 조회용 DTO
    @Data
    static class SimpleOrderDTO {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDTO(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName(); // LAZY 초기화
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getOrderStatus();
            this.address = order.getDelivery().getAddress(); // LAZY 초기화
        }
    }
}
