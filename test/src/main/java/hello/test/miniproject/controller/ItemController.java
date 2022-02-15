package hello.test.miniproject.controller;

import hello.test.miniproject.ItemData;
import hello.test.miniproject.domain.Item;
import hello.test.miniproject.repository.MemoryItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final MemoryItemRepository store;

    @PostMapping("/save")
    public String saveItem(@RequestBody ItemData itemData) {
        log.info("itemData = {}", itemData);
        Item item = new Item(itemData.getName(), itemData.getPrice(), itemData.getQuantity());
        store.itemSave(item);
        return "ok";
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<ItemData> findItemById(@PathVariable Long id) {
        Optional<Item> item = store.findById(id);
        ItemData itemData = new ItemData();
        if (item.isEmpty()) {
            return new ResponseEntity<ItemData>(HttpStatus.BAD_REQUEST);
        }

        Item findItem = item.get();
        itemData.setName(findItem.getName());
        itemData.setPrice(findItem.getPrice());
        itemData.setQuantity(findItem.getQuantity());

        return new ResponseEntity<ItemData>(itemData, HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public List<Item> findAllItem() {
        return store.findAll();
    }

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        Optional<Item> findItem = store.findById(id);
        if (findItem.isEmpty()) {
            return "not found item";
        }
        store.deleteItem(id);
        return "delete item id = " + id;
    }

    @PostConstruct
    private void init() {
        System.out.println("ItemController.init");
        Item test1 = new Item("test1", 10000, 1);
        Item test2 = new Item("test2", 20000, 2);
        Item test3 = new Item("test3", 30000, 3);
        store.itemSave(test1);
        store.itemSave(test2);
        store.itemSave(test3);
    }
}
