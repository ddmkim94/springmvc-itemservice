package hello.itemservice.service;

import hello.itemservice.domain.item.Book;
import hello.itemservice.domain.item.Item;
import hello.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity, String author, String isbn) {
        Book findItem = (Book) itemRepository.findById(itemId);
        findItem.changeBook(name, price, stockQuantity, author, isbn);
    }


    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findById(Long id) {
        return itemRepository.findById(id);
    }
}
