package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

//    //의존관계 주입(생략가능)
//    @Autowired
//    public BasicItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    }

    //상품의 목록
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(Model model, @PathVariable Long itemId) {
        Item findItem = itemRepository.findById(itemId);
        model.addAttribute("item", findItem);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addFormItem(Model model) {
        return "basic/addForm";
    }

//    @PostMapping("/add")
//    public String addItemV1(@ModelAttribute("item") Item item, Model model) {
//        itemRepository.save(item);
//        model.addAttribute("item", item);
//        return "/basic/item";
//    }

    @PostMapping("/add")
    public String addItem(Item item) {
        itemRepository.save(item);
        return "redirect:/basic/items";
    }

    @GetMapping("/{itemId}/edit")
    public String editItemForm(Model model, @PathVariable Long itemId) {
        Item findItem = itemRepository.findById(itemId);
        model.addAttribute("item", findItem);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String editItem(@ModelAttribute Item item, @PathVariable Long itemId, Model model) {
        itemRepository.update(itemId, item);
//        List<Item> items = itemRepository.findAll();
//        model.addAttribute("items", items);
        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/clear")
    public String clearItem() {
        itemRepository.clearStore();
        return "redirect:/basic/items";
    }

    @GetMapping("/{itemId}/delete")
    public String deleteItem(@PathVariable Long itemId) {
        itemRepository.itemDelete(itemId);
        return "redirect:/basic/items";
    }
    //생성자 실행 전에 실행
    /**
     * 테스트용 코드
     */
    @PostConstruct
    public void init() {
        Item itemA = new Item("itemA", 10000, 1);
        itemRepository.save(itemA);
        itemRepository.save(new Item("itemB", 20000, 2));
    }

}
