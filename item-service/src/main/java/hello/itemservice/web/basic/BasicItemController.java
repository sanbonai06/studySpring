package hello.itemservice.web.basic;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.*;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
@Slf4j
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
        model.addAttribute("item", new Item());
        return "basic/addForm";
    }

//    @PostMapping("/add")
//    public String addItemV1(@ModelAttribute("item") Item item, Model model) {
//        itemRepository.save(item);
//        model.addAttribute("item", item);
//        return "/basic/item";
//    }

//    @PostMapping("/add")
//    public String addItem(@ModelAttribute("item") Item item) {
//        itemRepository.save(item);
//        return "redirect:/basic/items";
//    }

    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, RedirectAttributes redirectAttributes) {
        log.info("item open = {}", item.getOpen());
        log.info("item deliveryCode= {}", item.getDeliveryCode());
        Item saveItem = itemRepository.save(item);
        //redirectAttribute에 들어간 값 중 redirect URL에 못 들어간 값들은 쿼리파라미터로 넘어간다.
        redirectAttributes.addAttribute("itemId", saveItem.getItemId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editItemForm(Model model, @PathVariable Long itemId) {
        Item findItem = itemRepository.findById(itemId);
        model.addAttribute("item", findItem);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String editItem(@ModelAttribute Item item, @PathVariable Long itemId, Model model) {
        log.info("itemType = {}", item.getItemType());
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

    @ModelAttribute("regions")
    public Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        regions.put("ANSAN", "안산");

        return regions;
    }

    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        return ItemType.values();
    }

//    @ModelAttribute("deliveryCode")
//    public Map<String, String> deliveryCode() {
//        Map<String, String> deliveryCodes = new LinkedHashMap<>();
//        deliveryCodes.put("FAST", "빠른배송");
//        deliveryCodes.put("SLOW", "느린 배송");
//        deliveryCodes.put("NORMAL", "일반 배송");
//
//        return deliveryCodes;
//    }

    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCode() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));

        return deliveryCodes;
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
