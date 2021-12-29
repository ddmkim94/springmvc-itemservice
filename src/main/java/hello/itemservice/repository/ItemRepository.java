package hello.itemservice.repository;

import hello.itemservice.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>();
    private static long sequence = 0L;


    // 상품 등록
    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    // 상품 상세 정보
    public Item findById(Long id) {
        return store.get(id);
    }

    // 상품 수정
    public void updateOne(Long id, Item updateParam) {
        Item findItem = store.get(id);
        findItem.setName(updateParam.getName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    // 상품 목록
    public List<Item> findAll() {
        List<Item> items = new ArrayList<>();
        for (Long id : store.keySet()) {
            items.add(store.get(id));
        }

        return items;
    }

    // 상품 제거
    public void clearStore() {
        store.clear();
    }
}
