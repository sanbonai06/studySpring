package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ItemRepository {

//    private static final Map<Long, Item> store = new ConcurrentHashMap<>();
    private static final Map<Long, Item> store = new HashMap<>();
    private static long sequence = 0L;

    public Item save(Item item) {
        item.setItemId(++sequence);
        store.put(item.getItemId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Item updateParam) {
        Item updateItem = findById(itemId);
        updateItem.setItemName(updateParam.getItemName());
        updateItem.setPrice(updateParam.getPrice());
        updateItem.setQuantity(updateParam.getQuantity());
    }

    public void itemDelete(Long itemId) {
        store.remove(itemId);
    }

    public void clearStore() {
        store.clear();
        sequence = 0L;
    }
}
