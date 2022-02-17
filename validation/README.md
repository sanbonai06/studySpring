# Validation 처리

## 컨트롤러에서 발생 한 오류를 뷰에서 보여주기 (타임리프)

* 글로벌 오류
```
<div th:if="${#fields.hasGlobalErrors()}">
            <p class="field-error" th:each="err : ${#fields.globalErrors()}" th:text="${err}">글로벌 오류 메시지</p>
</div>
```
* 필드 오류
```
<input type="text" id="itemName" th:field="*{itemName}" th:errorclass="field-error" class="form-control" placeholder="이름을 입력하세요">
            <div class="field-error" th:errors="*{itemName}">상품명 오류</div>
```

- th:errorclass는 오류가 발생하면 이미 선언된 class 속성 값에 field-error 값을 더해준다. 

## 컨트롤러에서 오류 처리 방법 (Validation)

```
@PostMapping("/add")
 public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {}
```

- BindingResult: Errors 인터페이스를 상속받으며 오류 저장 조회 및 다양한 기능들을 제공함,
	addError()를 통해 오류 메시지를 넘김 

- fieldError는 rejectValue()로, ObjectError는 reject()로 넘긴다. 

- 앞에서 배운 메시지 기능으로 오류 메시지를 넘길 수 있음 

```
#error.properties
required.item.itemName=상품 이름은 필수입니다.
range.item.price=가격은 {0} ~ {1} 까지 허용합니다.


#ItemValidator
bindingResult.rejectValue("itemName", "required");
bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
```

* ItemValidator를 컨트롤러에서 사용하는 방법

- 컨트롤러에 모든 검증 로직을 사용하면 코드가 복잡하고 컨트롤러가 하는일이 과중되므로 
따로 클래스를 생성하여 만든다. 해당 Validator처리 클래스는 Errors 인터페이스를 상속받는다.

- 사용방법

먼저 ItemValidator를 컨트롤러에 의존관계 주입한다. (@RequiredArgsConstructor가 선언되어 있으므로 private final로 선언해놓으면 생성자 주입으로 주입된다.)

```
@InitBinder
    public void init(WebDataBinder webDataBinder) {
        log.info("init binder = {}", webDataBinder);
        webDataBinder.addValidators(itemValidator);
    }
```
선언 후 호출되는 메서드에 @Validated 어노테이션을 붙인다. 

@Validated 어노테이션은 위 코드에서 등록한 WebDataBinder에 등록한 검증기(itemValidator)를 찾아 실행하는데 여러 검증기가 등록되면 itemValidator에서 사용하는 supports 메서드를 사용해 해당 클래스가 맞는지 검증한다. 

```
@PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {}
```

## Bean Validation 처리

* 앞의 코드와 같이 검증 기능을 매번 코드로 작성하기 귀찮고 힘들기 때문에 스프링에서 제공하는 Bean Validation을 활용하자. 

* 먼저 build.gradle에 의존관계를 추가하자
```
implementation 'org.springframework.boot:spring-boot-starter-validation'
```
* 필드 오류는 객체 자체에서, 글로벌 오류는 컨트롤러에서 자바 코드로 직접 작성하는 것이 효율적
* 같은 Item 객체로 validation처리를 하면 한계가 발생한다. -> 등록, 수정의 요구사항이 다를 경우 처리가 불가능하다.

=> 해결책: 1. BeanValidation의 groups 기능을 사용하자.
```
// 저장용 groups 생성
public interface SaveCheck {}

// Item 객체 
@Data
  public class Item {
@NotNull(groups = UpdateCheck.class) //수정시에만 적용 private Long id;
      @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
      private String itemName;
      @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
      @Range(min = 1000, max = 1000000, groups = {SaveCheck.class,UpdateCheck.class})
      private Integer price;
      @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
      @Max(value = 9999, groups = SaveCheck.class)
      //등록시에만 적용
      private Integer quantity;
//컨트롤러 
      PostMapping("/add")
  public String addItemV2(@Validated(SaveCheck.class) @ModelAttribute Item item,
  BindingResult bindingResult, RedirectAttributes redirectAttributes) {
//...
 }
```

2. Item 객체를 ItemSaveForm, ItemUpdateForm으로 별도의 모델 객체를 만들어 사용하자.(실무에서는 등록이나 수정 시 Item객체의 모든 정보들을 쓰지 않으므로
		등록과 수정 시 별도의 객체를 만들어서 전달한다.)

- HTML FORM -> ItemSaveForm -> Controller -> Item 생성 -> Repository 

```
//ItemSaveForm
//등록 시 필요한 정보들과 Validation 처리 적용
@Data
  public class ItemSaveForm {
      @NotBlank
      private String itemName;
      @NotNull
      @Range(min = 1000, max = 1000000)
      private Integer price;
      @NotNull
      @Max(value = 9999)
      private Integer quantity;
  }

//Controller
PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form,
BindingResult bindingResult, RedirectAttributes redirectAttributes) {}

```
- new Item() 으로 새 Item 객체 생성 후 Repository에 넘김.

* @ModelAttribute("item")에서 ("item")이 빠질 경우 itemSaveForm이라는 이름으로 model에 담겨 view로 넘어가기 때문에 주의하자

* @Valid, @Validated는 @RequestBody에도 적용 가능하다. 
