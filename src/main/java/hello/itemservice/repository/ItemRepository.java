package hello.itemservice.repository;

import hello.itemservice.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ItemRepository {

    private static Map<Long, Item> store = new HashMap<>();
    private static long sequence = 0L;


    // 상품 등록
    public void save(Item item) {
        store.put(++sequence, item);
    }

    // 상품 상세 정보
    public Item findOne(Long id) {
        return store.get(id);
    }

    // 상품 수정
    public Item updateOne(Item item) {
        Item findItem = store.get(item.getId());
        findItem.setName(item.getName());
        findItem.setPrice(item.getPrice());
        findItem.setQuantity(item.getQuantity());

        return findItem;
    }

    // 상품 목록
    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        for (Long id : store.keySet()) {
            items.add(store.get(id));
        }

        return items;
    }
}
