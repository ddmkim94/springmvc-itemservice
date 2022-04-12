package hello.itemservice.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderFlatDTO> findAllByDto_flat() {
        return em.createQuery("select " +
                " new hello.itemservice.repository.order.query.OrderFlatDTO(o.id, m.name, o.orderDate, o.orderStatus, d.address, i.name, i.price, oi.count)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d" +
                " join o.orderItems oi" +
                " join oi.item i", OrderFlatDTO.class)
                .getResultList();
    }

    public List<OrderQueryDTO> findAllByDto_optimization() {
        // 루트 조회! (toOne 코드를 한 번에 조회)
        List<OrderQueryDTO> orders = findOrders();

        Map<Long, List<OrderItemQueryDTO>> orderItemMap = findOrderItemMap(toOrderIds(orders));

        for (OrderQueryDTO order : orders) {
            order.setOrderItems(orderItemMap.get(order.getOrderId()));
        }

        return orders;
    }

    private List<Long> toOrderIds(List<OrderQueryDTO> orders) {
        List<Long> orderIds = new ArrayList<>();
        for (OrderQueryDTO order : orders) {
            orderIds.add(order.getOrderId());
        }
        return orderIds;
    }

    private Map<Long, List<OrderItemQueryDTO>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDTO> orderItems = em.createQuery("select new hello.itemservice.repository.order.query.OrderItemQueryDTO(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                " from OrderItem oi " +
                " join oi.item i" +
                " where oi.order.id in :orderIds", OrderItemQueryDTO.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        return orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDTO::getOrderId));
    }

    public List<OrderQueryDTO> findOrderQueryDtos() {
        List<OrderQueryDTO> findOrders = findOrders();

        // 루프를 돌면서 컬렉션 추가 (추가 쿼리 실행)
        for (OrderQueryDTO order : findOrders) {
            List<OrderItemQueryDTO> findOrderItems = findOrderItems(order.getOrderId());
            order.setOrderItems(findOrderItems);
        }

        return findOrders;
    }

    private List<OrderItemQueryDTO> findOrderItems(Long orderId) {
        return em.createQuery("select new hello.itemservice.repository.order.query.OrderItemQueryDTO(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                " from OrderItem oi " +
                " join oi.item i" +
                " where oi.order.id = :orderId", OrderItemQueryDTO.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDTO> findOrders() {
        return em.createQuery("select new hello.itemservice.repository.order.query.OrderQueryDTO(o.id, m.name, o.orderDate, o.orderStatus, d.address)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d", OrderQueryDTO.class).getResultList();
    }
}
