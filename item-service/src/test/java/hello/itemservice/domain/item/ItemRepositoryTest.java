package hello.itemservice.domain.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    public void clearStore() {
        itemRepository.clearStore();
    }

    @Test
    public void saveTest() {
        Item item = new Item("itemA", 2000, 3);
        Item saveItem = itemRepository.save(item);

        assertThat(saveItem.getItemId()).isEqualTo(item.getItemId());
    }

    @Test
    public void findByIdTest() {
        Item item1 = new Item("itemA", 2000, 3);
        Item item2 = new Item("itemB", 3000, 4);

        Item saveItem1 = itemRepository.save(item1);
        Item saveItem2 = itemRepository.save(item2);

        Item findItem = itemRepository.findById(item2.getItemId());

        assertThat(findItem.getItemName()).isEqualTo(item2.getItemName());
    }

    @Test
    public void findAllTest() {
        Item item1 = new Item("itemA", 2000, 3);
        Item item2 = new Item("itemB", 3000, 4);
        Item item3 = new Item("itemC", 3000, 4);

        Item saveItem1 = itemRepository.save(item1);
        Item saveItem2 = itemRepository.save(item2);
        Item saveItem3 = itemRepository.save(item3);

        List<Item> items = itemRepository.findAll();

        assertThat(items.size()).isEqualTo(3);
    }

    @Test
    public void updateTest() {
        Item item1 = new Item("itemA", 2000, 3);
        Item item2 = new Item("itemB", 3000, 4);

        Item saveItem = itemRepository.save(item1);

        Long itemId = saveItem.getItemId();

        itemRepository.update(itemId, item2);

        Item find = itemRepository.findById(itemId);

        assertThat(find.getItemName()).isEqualTo(item2.getItemName());

    }
}
