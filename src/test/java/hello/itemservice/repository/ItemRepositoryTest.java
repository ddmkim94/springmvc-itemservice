package hello.itemservice.repository;

import hello.itemservice.domain.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryTest {

    private final ItemRepository itemRepository = new ItemRepository();

    // 테스트 메서드가 끝날때마다 실행 -> store 저장소 초기화
    @AfterEach
    public void afterEach() {
        itemRepository.clearStore();
    }

    @Test
    void save() {
        Item item = new Item(1L, "iPad Pro", 1000000, 10);
        Item findItem = itemRepository.save(item);

        assertThat(item.getId()).isEqualTo(findItem.getId());
        assertThat(item.getName()).isEqualTo(findItem.getName());
        assertThat(item.getPrice()).isEqualTo(findItem.getPrice());
        assertThat(item.getQuantity()).isEqualTo(findItem.getQuantity());
        assertSame(item, findItem); // 같은 객체인지 검증
    }

    @Test
    void findAll() {
        Item item1 = new Item(1L, "iPad Pro", 1000000, 10);
        Item item2 = new Item(2L, "MacBook Pro", 2000000, 10);

        itemRepository.save(item1);
        itemRepository.save(item2);

        List<Item> items = itemRepository.findAll();

        assertThat(items.size()).isEqualTo(2);
        assertThat(items).contains(item1, item2);
    }

    @Test
    void updateItem() {
        Item item1 = new Item(1L, "iPad Pro", 1000000, 10);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("MacBook Pro");
        item2.setPrice(2000000);
        item2.setQuantity(100);

        itemRepository.updateOne(item1.getId(), item2); // 수정

        assertThat(item1.getName()).isEqualTo(item2.getName());
    }

}