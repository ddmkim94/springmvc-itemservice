package hello.itemservice.controller;

import hello.itemservice.domain.item.Book;
import hello.itemservice.domain.item.Item;
import hello.itemservice.service.ItemService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(@ModelAttribute("form") BookForm form) {
        Book book = new Book(form.getName(), form.getPrice(), form.getStockQuantity(), form.getAuthor(), form.getIsbn());
        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{id}/edit")
    public String updateItemForm(Model model, @PathVariable("id") Long id) {
        Book item = (Book) itemService.findById(id);

        BookForm form = new BookForm(item.getId(),
                item.getName(), item.getPrice(), item.getStockQuantity(), item.getAuthor(), item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{id}/edit")
    public String updateItem(@ModelAttribute("form") BookForm form) {
        itemService.updateItem(form.getId(), form.getName(), form.getPrice(), form.getStockQuantity(), form.getAuthor(), form.getIsbn());
        return "redirect:/items";
    }
}