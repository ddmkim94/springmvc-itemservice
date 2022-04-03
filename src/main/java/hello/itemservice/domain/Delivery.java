package hello.itemservice.domain;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
public class Delivery {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(STRING)
    private DeliveryStatus deliveryStatus;

    public void addOrder(Order order) {
        this.order = order;
    }
}
