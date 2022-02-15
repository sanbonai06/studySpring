# 타임리프 공부

## 타임리프 스프링 통합
* 타임리프의 여러가지 입력 폼 처리 기능

- th:object: 커맨드 객체를 지정

- *{...}: th:object에서 선택한 객체에 접근(<form>에서 사용할 객체를 지정) th:object를 사용하려면 해당 객체의 정보를 뷰로 넘겨줘야 한다. (model.addatribute를 이용하자) 

- th:field: HTML 태그 id, name, value 속성을 자동으로 처리해줌

- <input type="text" th:field="*{itemName}" /> => <input type="text" id="itemName" name="itemName" th:value="*{itemName}" />

* ENUM: 고정 상수화된 집합이 필요할 때 ENUM을 사용한다. 

- Enum은 static final 하다.

- 계승(상속)이 불가능하다.

-컴파일 타임, 타입 세이프하다.

-그룹핑이 가능하다(Enum의 Enum을 하여 조합 가능).

-Object를 계상받아 Object에서 제공하는 메소드를 활용할 수 있다. 또는 디폴트 메소드를 사용할 수 있다.

-serializable, comparable이 가능하다.

-메소드를 사용하여 기능 확장이 무궁무진하다(상수 + 관련 데이터의 연계 및 연산을 사용할 수 있다.

-비교 연산은 Int 상수와 성능이 비슷하다.

```
package hello.itemservice.domain.item;

public enum ItemType {
    BOOK("도서"), FOOD("식품"), ETC("기타");

    private final String description;

    ItemType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
```

* 체크박스 - 단일 
- HTML 체크박스는 선택이 안됐을 때, 클라단에서 서버단으로 값 자체를 보내지 않음(로그 찍어보면 null로 받음) => 수정할 때 문제 발생 
이 때 _open처럼 기존 체크 박스의 이름앞에 언더바를 붙여서 전송 시 체크를 해제했다고 인식 가능(히든 필드) 

타임 리프 미사용 시 히든 필드 사용

<input type="checkbox" id="open" name="open" class="form-check-input">

<input type="hidden" name="_open" value="on"/>

타임 리프 사용

<input type="checkbox" th:field="*{open}" class="form-check-input"> 이 한줄로 히든 필드까지 생성

* 체크박스 - 멀티

- @ModelAttribute는 컨트롤러에 있는 별도의 메서드에 적용 가능 => 해당 컨트롤러를 요청 할 때 반환값이 자동으로 model.addAttribute에 넘긴것과 똑같은 효과

```
<div th:each="region : ${regions}" class="form-check form-check-inline">
          <input type="checkbox" th:field="*{regions}" th:value="${region.key}"
  class="form-check-input">
          <label th:for="${#ids.prev('regions')}"
      th:text="${region.value}" class="form-check-label">서울</label>
```

위 코드와 같이 th:each로 선언 후 접근

th:for="${#ids.prev('regions')}" => 멀티 박스는 HTML 태그에서 name은 같아도 되지만, id 태그는 달라야 한다. 따라서 타임리프는 임의로 1,2,3 숫자를 붙여준다.

* 라디오 버튼
- 여러 선택지 중에 하나를 선택 ENUM을 이용해서 만들어 보자.

```

  @ModelAttribute("itemTypes")
  public ItemType[] itemTypes() {
      return ItemType.values();
  }
```

```
<div th:each="type : ${itemTypes}" class="form-check form-check-inline">
<input type="radio" th:field="*{itemType}" th:value="${type.name()}" class="form-check-input">
<label th:for="${#ids.prev('itemType')}" th:text="${type.description}" class="form-check-label">BOOK</label>
</div>
</div>

* 셀렉트 박스
- 여러 선택지 중 하나를 선택 할 때 사용

```
package hello.itemservice.domain.item;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeliveryCode {

    private String code;
    private String displayName;
}
```

```
<div>
            <div>배송 방식</div>
            <select th:field="*{deliveryCode}" class="form-select">
                <option value="">==배송 방식 선택==</option>
                <option th:each="deliveryCode : ${deliveryCodes}" th:value="${deliveryCode.code}" th:text="${deliveryCode.displayName}">
                    FAST
                </option>
            </select>
        </div>
```

* 주의 사항: th:object가 선언되어있는 <form>형태가 없는 HTML 코드에서는 th:field="*{} 가 사용 불가능 => th:field="${item....}"을 이용해야함 

