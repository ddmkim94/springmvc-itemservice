package hello.itemservice;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class SampleData {

    private final ItemRepository itemRepository;

    // 스프링이 올라오고 나서 실행되게 해주는 어노테이션
    @PostConstruct
    public void init() {
        Item item1 = new Item(1L, "iPad Pro", 1000000, 10);
        Item item2 = new Item(2L, "MacBook Pro", 2000000, 20);
        Item item3 = new Item(3L, "Apple Watch", 500000, 30);
        Item item4 = new Item(4L, "AirPods Pro", 300000, 40);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.save(item4);
    }
}
