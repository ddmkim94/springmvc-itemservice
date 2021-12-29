package hello.itemservice.controller;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;

    // 상품 목록
    @GetMapping("basic/items")
    public String itemList(Model model) {

        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "items";
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
}
