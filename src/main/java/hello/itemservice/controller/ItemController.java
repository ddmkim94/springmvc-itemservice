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
}
