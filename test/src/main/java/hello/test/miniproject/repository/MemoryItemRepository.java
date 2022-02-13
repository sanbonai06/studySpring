package hello.test.miniproject.repository;

import hello.test.miniproject.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryItemRepository implements ItemRepository{

    private static Map<Long, Item> store = new HashMap<>();
    private static Long sequence = 100L;

    @Override
    public Item itemSave(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable (store.get(id));
    }

    @Override
    public Optional<Item> findByName(String name) {
       return store.values().stream()
                .filter(item -> item.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<Item>(store.values());
    }

    @Override
    public void deleteItem(Long id) {
        store.remove(id);
    }

    @Override
    public void clearItem() {
        store.clear();
    }
}
