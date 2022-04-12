package hello.itemservice.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

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
