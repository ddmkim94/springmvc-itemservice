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

    // 상품 상세 폼
    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    /**
     * 상품 등록 처리 V1 (@RequestParam)
     * 요청 데이터의 이름과 파라미터의 이름을 똑같이 설정하는 경우 @RequestParam 속성 생략 가능
      */
    // @PostMapping("/add")
    public String addItemV1(@RequestParam("name") String name,
                            @RequestParam int price,
                            @RequestParam int quantity,
                            Model model) {

        Item item = new Item(name, price, quantity);
        itemRepository.save(item);
        model.addAttribute("item", item);
        return "basic/item";
    }

    // 상품 등록 처리 V2 (@ModelAttribute)
    // @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
        Item saveItem = itemRepository.save(item);
        model.addAttribute("item", saveItem);
        return "basic/item";
    }

    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);
        // @ModelAttribute가 model에 지정한 객체를 자동으로 넣어줌
        return "basic/item";
    }

    // @PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);
        return "basic/item";
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
