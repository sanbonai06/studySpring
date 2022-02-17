package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/validation/v3/items")
@RequiredArgsConstructor
@Slf4j
public class ValidationItemControllerV3 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v3/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v3/addForm";
    }

@PostMapping("/add")
    public String addItem(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

    //글로벌 오류 부분은 자바 코드로 직접 작성
    if (item.getPrice() != null && item.getQuantity() != null) {
        int resultValue = item.getPrice() * item.getQuantity();
        if (resultValue < 10000) {
            bindingResult.reject("totalPriceMin", new Object[]{10000, resultValue}, null);
        }
    }

    //에러 발생 시 다시 입력 폼으로 돌아가기
    if(bindingResult.hasErrors()) {
        log.info("error = {}", bindingResult);
        //bindingResult는 model에 안담아도 자동으로 view로 넘어감
        return "validation/v3/addForm";
    }

    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v3/items/{itemId}";
}

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v3/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated(UpdateCheck.class) @ModelAttribute Item item, BindingResult bindingResult) {
        //글로벌 오류 부분은 자바 코드로 직접 작성
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultValue = item.getPrice() * item.getQuantity();
            if (resultValue < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultValue}, null);
            }
        }

        //에러 발생 시 다시 입력 폼으로 돌아가기
        if(bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult);
            //bindingResult는 model에 안담아도 자동으로 view로 넘어감
            return "validation/v3/editForm";
        }

        itemRepository.update(itemId, item);
        return "redirect:/validation/v3/items/{itemId}";
    }

}

