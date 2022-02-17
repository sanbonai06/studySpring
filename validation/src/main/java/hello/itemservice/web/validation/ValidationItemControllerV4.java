package hello.itemservice.web.validation;

import hello.itemservice.domain.item.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
@Slf4j
public class ValidationItemControllerV4 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v4/addForm";
    }

@PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

    //글로벌 오류 부분은 자바 코드로 직접 작성
    if (form.getPrice() != null && form.getQuantity() != null) {
        int resultValue = form.getPrice() * form.getQuantity();
        if (resultValue < 10000) {
            bindingResult.reject("totalPriceMin", new Object[]{10000, resultValue}, null);
        }
    }

    //에러 발생 시 다시 입력 폼으로 돌아가기
    if(bindingResult.hasErrors()) {
        log.info("error = {}", bindingResult);
        //bindingResult는 model에 안담아도 자동으로 view로 넘어감
        return "validation/v4/addForm";
    }

    Item item = new Item(form.getItemName(), form.getPrice(), form.getQuantity());
    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v4/items/{itemId}";
}

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) {
        //글로벌 오류 부분은 자바 코드로 직접 작성
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultValue = form.getPrice() * form.getQuantity();
            if (resultValue < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultValue}, null);
            }
        }

        //에러 발생 시 다시 입력 폼으로 돌아가기
        if(bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult);
            //bindingResult는 model에 안담아도 자동으로 view로 넘어감
            return "validation/v4/editForm";
        }

        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());

        itemRepository.update(itemId, item);
        return "redirect:/validation/v4/items/{itemId}";
    }

}

