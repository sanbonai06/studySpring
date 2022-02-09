# Spring-MVC 기본기능

## 로그
* @Slf4j 롬복에서 제공하는 어노테이션으로 사용 가능
* 로그 레벨: TRACE > DEBUG > INFO > WARN > ERROR 순 

-> application.properties에서 로그 레벨을 설정하면 그보다 낮은 레벨들의 로그들을 보여줌
```
logging.level.파일경로 = 로그레벨
```

* 로그 사용법
```
log.info("data = {}", data)
```

## 요청 매핑
* @Controller 어노테이션이 붙은 컨트롤러에서 메서드의 반환값이 String이면 뷰 이름으로 인식 => 반환값으로 뷰를 찾고 랜더링함
* @RestController 어노테이션이 붙은 컨트롤러는 반환값 자체가 Http 응답 메시지 바디로 입력 
* @RequestMapping("/hello-basic"): hello-basic으로 호출이 오면 메서를 실행하도록 매핑함(배열로 다중 설정 가능)

-> @GetMapping, @PostMapping 처럼 Http 메서드를 붙여서 쓰는게 일반적.

* Pathvariable 사용: 

```
@GetMapping("/mapping/{userId}")
  public String mappingPath(@PathVariable("userId") String data) {
      log.info("mappingPath userId={}", data);
      return "ok";
  }
```

-> 다중:

```

  @GetMapping("/mapping/users/{userId}/orders/{orderId}")
  public String mappingPath(@PathVariable String userId, @PathVariable Long
  orderId) {
      log.info("mappingPath userId={}, orderId={}", userId, orderId);
      return "ok";
  }
```

* MultiValueMap: Map과 유사하지만 하나의 키에 여러개의 값을 담을 수 있는점에서 다름.(쿼리 파라미터가 여러개일 때 사용)
```
MultiValueMap<String, String> map = new LinkedMultiValueMap();
  map.add("keyA", "value1");
  map.add("keyA", "value2");
  //[value1,value2]
  List<String> values = map.get("keyA");	//keyA에 여러개의 값이 담겨있으므로 List로 받아옴
```

## Http 요청 파라미터

### 쿼리파라미터, HTML Form

* GET - 쿼리 파라미터

* POST - HTML Form: content-type: application/x-www-form-urlencoded, 메시지 바디에 쿼리 파라미터 형식으로 전달
```
/**
* @RequestParam 사용
* - 파라미터 이름으로 바인딩
* @ResponseBody 추가
* - View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력 */
    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
    @RequestParam("username") String memberName,
    @RequestParam("age") int memberAge) {
     log.info("username={}, age={}", memberName, memberAge);
     return "ok";
 }

/**
* @RequestParam 사용
* HTTP 파라미터 이름이 변수 이름과 같으면 @RequestParam(name="xx") 생략 가능 */
  @ResponseBody
  @RequestMapping("/request-param-v3")
  public String requestParamV3(
          @RequestParam String username,
          @RequestParam int age) {
      log.info("username={}, age={}", username, age);
      return "ok";
}

/**
* @RequestParam 사용
* String, int 등의 단순 타입이면 @RequestParam 도 생략 가능 */
  @ResponseBody
  @RequestMapping("/request-param-v4")
  public String requestParamV4(String username, int age) {
      log.info("username={}, age={}", username, age);
      return "ok";
  }
```

-> @ResponseBody: @RestController처럼 view 이름을 조회하지 않고 Http message body에 직접 입력

-> @RequestParam(required - false): 기본값은 true, false면 필수값이 아님 

-> @RequestParam(defaultValue = -1): 파라미터의 값이 null이나 ""일 때, 디폴트 값을 지정해줌 				  

-> 파라미터를 Map 받아오기
```
/**
   * @RequestParam Map, MultiValueMap
   * Map(key=value)
   * MultiValueMap(key=[value1, value2, ...] ex) (key=userIds, value=[id1, id2])
   */
  @ResponseBody
  @RequestMapping("/request-param-map")
  public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
      log.info("username={}, age={}", paramMap.get("username"),
  paramMap.get("age"));
      return "ok";
  }
```
파라미터의 값이 여러개 일수 있다면 MultiValueMap을 사용

-> @ModelAttribute를 써보자
```
//@Data 어노테이션을 사용하면 자동으로 getter, setter 등등을 적용해준다. 
@Data
public class HelloData {
	private String username;
	private int age;
}

/**
* @ModelAttribute 생략 가능
* String, int 같은 단순 타입 = @RequestParam
* argument resolver 로 지정해둔 타입 외 = @ModelAttribute */
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) {
        log.info("username={}, age={}", helloData.getUsername(),
    helloData.getAge());
        return "ok";
    }
```
* Http message body에 데이터를 직접 요청: Rest API, 데이터 형식은 주로 JSON



