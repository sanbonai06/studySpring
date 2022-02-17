package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
@Slf4j
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    //@PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult,  RedirectAttributes redirectAttributes, Model model) {
        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", "상품이름은 필수입니다."));

        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", "상품가격은 1000원 이상 1000000원 이하입니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 9999개까지 등록가능합니다."));
        }

        //복합적 검증
        if (item.getPrice() != null && item.getItemName() != null) {
            if (item.getPrice() * item.getQuantity() < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량은 10000원 이상이어야 합니다. 현재값은 " + item.getQuantity() * item.getPrice() + "원 입니다."));
            }
        }

        //에러 발생 시 다시 입력 폼으로 돌아가기
        if (bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult);
            //bindingResult는 model에 안담아도 자동으로 view로 넘어감
            return "validation/v2/addForm";
        }
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
        //@PostMapping("/add")
        public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult,  RedirectAttributes redirectAttributes, Model model) {
        //검증 로직
        if(!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품이름은 필수입니다."));

        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "상품가격은 1000원 이상 1000000원 이하입니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() > 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 9999개까지 등록가능합니다."));
        }

        //복합적 검증
        if(item.getPrice() != null && item.getItemName() != null) {
            if (item.getPrice() * item.getQuantity() < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량은 10000원 이상이어야 합니다. 현재값은 " + item.getQuantity() * item.getPrice() + "원 입니다."));
            }
        }

        //에러 발생 시 다시 입력 폼으로 돌아가기
        if(bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult);
            //bindingResult는 model에 안담아도 자동으로 view로 넘어감
            return "validation/v2/addForm";
        }


        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult,  RedirectAttributes redirectAttributes, Model model) {
    //검증 로직
//    if(!StringUtils.hasText(item.getItemName())) {
//        bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품이름은 필수입니다."));
//
//    }
    if (!StringUtils.hasText(item.getItemName())) {
        bindingResult.addError(new FieldError("item", "itemName", item.getItemName(),
                false, new String[]{"required.item.itemName"}, null, null));
    }
    if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
        bindingResult.addError(new FieldError("item", "price", item.getPrice(),
                false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
        // bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "상품가격은 1000원 이상 1000000원 이하입니다."));
    }
    if (item.getQuantity() == null || item.getQuantity() > 9999) {
        bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(),
                false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
        // bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 9999개까지 등록가능합니다."));
    }

    //복합적 검증
    if(item.getPrice() != null && item.getItemName() != null) {
        if (item.getPrice() * item.getQuantity() < 10000) {
            bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"},
                    new Object[]{10000, item.getPrice() * item.getQuantity()}, null));
            // bindingResult.addError(new ObjectError("item", "가격 * 수량은 10000원 이상이어야 합니다. 현재값은 " + item.getQuantity() * item.getPrice() + "원 입니다."));
        }
    }

    //에러 발생 시 다시 입력 폼으로 돌아가기
    if(bindingResult.hasErrors()) {
        log.info("error = {}", bindingResult);
        //bindingResult는 model에 안담아도 자동으로 view로 넘어감
        return "validation/v2/addForm";
    }


    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v2/items/{itemId}";
}
    //@PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult,  RedirectAttributes redirectAttributes, Model model) {
    //검증 로직
//    if(!StringUtils.hasText(item.getItemName())) {
//        bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품이름은 필수입니다."));
//
//    }
        log.info("object Name = {}", bindingResult.getObjectName());
        log.info("target = {}", bindingResult.getTarget());

    if (!StringUtils.hasText(item.getItemName())) {
        bindingResult.rejectValue("itemName", "required");
    }
    if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
        bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        // bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "상품가격은 1000원 이상 1000000원 이하입니다."));
    }
    if (item.getQuantity() == null || item.getQuantity() > 9999) {
        // bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 9999개까지 등록가능합니다."));
        bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
    }

    //복합적 검증
    if(item.getPrice() != null && item.getItemName() != null) {
        if (item.getPrice() * item.getQuantity() < 10000) {
            bindingResult.reject("totalPriceMin", new Object[]{10000, item.getPrice() * item.getQuantity()}, null);
            // bindingResult.addError(new ObjectError("item", "가격 * 수량은 10000원 이상이어야 합니다. 현재값은 " + item.getQuantity() * item.getPrice() + "원 입니다."));
        }
    }

    //에러 발생 시 다시 입력 폼으로 돌아가기
    if(bindingResult.hasErrors()) {
        log.info("error = {}", bindingResult);
        //bindingResult는 model에 안담아도 자동으로 view로 넘어감
        return "validation/v2/addForm";
    }


    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v2/items/{itemId}";
}

//@PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult,  RedirectAttributes redirectAttributes, Model model) {

    itemValidator.validate(item, bindingResult);

    //에러 발생 시 다시 입력 폼으로 돌아가기
    if(bindingResult.hasErrors()) {
        log.info("error = {}", bindingResult);
        //bindingResult는 model에 안담아도 자동으로 view로 넘어감
        return "validation/v2/addForm";
    }


    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v2/items/{itemId}";
}
@PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

    //에러 발생 시 다시 입력 폼으로 돌아가기
    if(bindingResult.hasErrors()) {
        log.info("error = {}", bindingResult);
        //bindingResult는 model에 안담아도 자동으로 view로 넘어감
        return "validation/v2/addForm";
    }


    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v2/items/{itemId}";
}

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @InitBinder
    public void init(WebDataBinder webDataBinder) {
        log.info("init binder = {}", webDataBinder);
        webDataBinder.addValidators(itemValidator);
    }
}

