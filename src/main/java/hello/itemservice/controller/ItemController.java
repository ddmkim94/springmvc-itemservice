package hello.itemservice.controller;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;

    // 상품 목록
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("basic/item-form")
    public String createItemForm() {
        return "createItemForm";
    }

    // 상품 등록 로직
    @PostMapping("basic/create")
    public String createItem(@ModelAttribute Item item, Model model) {

        Item saveItem = itemRepository.save(item);
        model.addAttribute("item", saveItem);
        return "itemDetailView";
    }

    // 상품 상세 폼
    @GetMapping("basic/detail-form")
    public String detailItemForm(@RequestParam("id") Long id, Model model) {

        Item findItem = itemRepository.findById(id);
        model.addAttribute("item", findItem);

        return "itemDetailView";
    }

    // 상품 수정 폼
    @GetMapping("basic/update-form")
    public String updateItemForm(@ModelAttribute Item item, Model model) {

        model.addAttribute("item", item);
        return "itemUpdateForm";
    }

    // 상품 수정
    @PostMapping("basic/update")
    public String updateItem(@ModelAttribute Item item) {

        Item updateItem = new Item(item.getName(), item.getPrice(), item.getQuantity());
        itemRepository.updateOne(item.getId(), updateItem);

        return "redirect:detail-form?id=" + item.getId();
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item(1L, "iPad Pro", 1000000, 10));
        itemRepository.save(new Item(2L, "MacBook Pro", 2000000, 20));
        itemRepository.save(new Item(3L, "Apple Watch", 500000, 30));
        itemRepository.save(new Item(4L, "AirPods Pro", 300000, 40));
    }
}
