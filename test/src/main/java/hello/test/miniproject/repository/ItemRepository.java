package hello.test.miniproject.repository;

import hello.test.miniproject.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item itemSave(Item item);

    Optional<Item> findById(Long id);

    Optional<Item> findByName(String name);

    List<Item> findAll();

    void deleteItem(Long id);

    void clearItem();
}
