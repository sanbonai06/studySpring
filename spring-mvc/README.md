# Spring-mvc 공부
* 공부방식: Servlet -> MVC 패턴 -> Spring-MVC 순으로 공부하면서 과거 방식의 문제점들과 발전되어가는 과정을 알아볼것임.

## Servlet
* 클라이언트의 요청을 처리하고 그 결과값을 반환하는 Servlet 클래스의 구현 규칙을 지킨 자바 웹 프로그래밍 기술
* main 파일에 @ServletComponentScan 어노테이션을 사용함으로써 동작
```
@WebServlet(name = "", urlPatterns = "")
public class xxx extends HttpServlet {
     @Override
     protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     }
}
```
위와 같은 코드를 작성하여 서블릿 코드 작성
* 동작방식: 클라이언트가 HTTP request를 보냄 -> Servlet Container가 전달받고 HttpServletRequest, HttpServletResponse 객체를 생성 -> 전달받은 request가 어느 서블릿에 대한 요청인지 찾아봄
-> 요청이 get, post 요청인지에 따라 doGet(), doPost()를 호출 -> 동적 페이지를 생성한 후 HttpServletResponse 객체에 응답을 보낸 후 req, res 객체 모두 소멸시킴
* HttpServletRequest, HttpServleteResponse 객체들은 개발자가 Http 응답 메시지를 편리하게 사용하도록 도와주는 객체이다.

( HttpServletRequest 객체는 임시 저장소 기능이 있음 -> req.setAttribute(key,values), req.getAttribute(key))

( HttpServletResponse 객체는 쿠키관리, Redirect 등의 편의 기능을 제공한다.)

-> 실제로 Http 요청 데이터에서 사용하는 쿼리파라미터, html 폼 데이터, json 데이터들을 Servlet을 통해 전달해 보면서 매우 불편함을 느꼈다. 이제 MVC 패턴으로 가보자.

## Servlet, Jsp, MVC 패턴
* MVC 패턴이란 Model, View, Controller 패턴으로써 서블릿 코드 같은 경우 한 파일에 너무 많은 과정들이 집중 되므로 유지보수에 어렵고 각 과정마다 라이프 사이클, 기능 특화등의 문제들 때문에  각각의 과정을 나눠서 관리하는 패턴이다.

Contorller(Servlet Container)에서 Http 요청을 받고 파라미터를 검증하고 비즈니스 로직을 실행함으로써 view에 전달해야 할 결과 데이터들을 model에 담아서 보냄.

-> Model은 view에서 출력할 데이터들을 담아두고 전달해주는 역할을 수행한다. Model 덕분에 view는 controller가 처리하는 비즈니스 로직과 데이터 접근들에 신경을 안쓰고 화면 렌더링에 집중 할 수 있다.

-> View는 모델에 담겨있는 데이터를 토대로 화면을 렌더링한다. (Html 생성)

실제로 비즈니스 로직과 데이터 접근은 한 Controller파일에서 일어나지 않고 따로 서비스 객체를 생성하여 진행한 후 결괏값들을 모델에 담는다.
* model의 역할은 HttpServletRequest 객체 내부에 있는 req.setAttribute가 맡았다. (꺼낼 때는 req.getAttribute())
* 참고 사항

-> /WEB-INF 파일 경로 내부에 JSP 파일이 있으면 외부에서 url로 직접 호출이 불가능해진다. -> controller를 통해 호출하기를 원함

-> /WEB-INF 내부에 있는 JSP파일은 forward 방식을 통해 호출한다.
```
String viewPath = "/WEB-INF/views/save-result.jsp";
RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath);
dispatcher.forward(req, res);
```
위 코드와 같은 방식으로 JSP 파일을 호출한다.

-> 리다이렉트 방식은 실제 클라이언트에 500번대 response를 보내면서 다시 요청을 받아 처리하는 방식이지만 포워드 방식은 서버 내부에서 일어나는 호출이라는 점에서 차이점이 있다.

* 하지만 이런 MVC 패턴 방식에도 극명한 한계가 있다.

-> 여러 코드들의 중복 (포워드 중복, viewPath 중복)들이 존재한다. 포워드 방식을 설명한 코드를 보면 저런 코드들이 반복되서 사용되는 것을 볼 수 있다.

-> HttpServletRequest, HttpServletResponse 등등과 같은 코드들이 매번 사용되는 것이 아니라 불필요 할 수도 있다.

-> 공통 처리가 어렵다. 기능이 더 다양한 프로젝트들을 작성할 때는 공통으로 처리해야하는 부분들이 늘어나기 때문에 공통 기능을 메서드로 뽑아야 하는데 문제가 많이 발생하고 또 공통 메서드를 항상 호출하는것 자체도 중복이다.

* 위의 문제점들을 해결하기 위해 수문장 역할을 하는 프론트 컨트롤러 패턴을 도입해야 한다. 따라서 이러한 패턴을 도입한 스프링 MVC를 공부해보도록 하자.

## MVC 프레임워크 만들기
* V1(프론트 컨트롤러 도입): MVC 패턴에서 발생한 문제점인 공통 처리를 하기위해 프론트 컨트롤러를 도입한다.(프론트 컨트롤러 서블릿 하나가 모든 요청을 받고 요청에 맞는 컨트롤러를 찾아서 호출함)

-> Http 요청 받음 => URL 매핑 정보에서 컨트롤러를 조회함 => 호출된 컨트롤러에서 JSP forward 실행 => JSP가 화면에 뜸

-> URL 매핑 정보에서 컨트롤러 조회 및 호출 방식
```
@WebServlet(name = "", urlPatterns = "/front-controller/v1/*")

private Map<String, ControllerV1> controllerMap = new HashMap<>();

    public FrontControllerServletV1() {
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerMap.put("/front-controller/v1/members", new MemberListControllerV1());
    }

String requestURI = req.getRequestURI();
ControllerV1 controller = controllerMap.get(requestURI);

if(controller == null) {
	res.setStatus(404);	//컨트롤러를 못찾으면 잘못된 요청이므로 404에러 반환
	return;
}

controller.process(req, res);
```

* V2(View 분리): V1에서 foward 부분의 중복이 있음 이를 해결하기 위해 view를 분리하자(Myview 생성)

-> Http 요청 받음 => URL 매핑 정보에서 컨트롤러를 조회 및 호출 => 컨트롤러에서 MyView를 반환해줌 => MyView.render()를 호출하면 JSP forward가 자동으로 실행 => JSP가 화면에 뜸

-> MyView 생성자에서 viewPath 변수를 넣어서 받아옴 => viewPath로 forward 실행

V* V3(Model 추가): 지금까지는 req.setAttribute를 이용하여 정보를 저장해서 jsp로 넘겼다. 이는 Servlet에 종속적이므로 Map<String, Object> model을 생성하여 사용할 것이다, view 이름도 계속 중복되므로 논리적 이름(Ex.new-form)만 반환할 것이다.

-> Http 요청 받음 => URL 매핑 정보에서 컨트롤러를 조회 및 호출 => ModelView 반환(view의 논리적 이름) => viewResolver를 호출 => MyView를 반환 => view.render(model) 호출 => MyView에서 jsp로 forward => JSP가 화면에 뜸

-> 컨트롤러에서 Servlet의 종속성을 없애기 위해 HttpServletRequest, HttpServletResponse를 사용하지 않고 Map<String, String> paramMap을 사용함: FrontController에서 request로 들어온 파라미터들을 paramMap에 넣어서 컨트롤러를 호출한다.
```
private Map<String, String> createParamMap(HttpServletRequest req) {
        Map<String, String> paramMap = new HashMap<>();
        req.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, req.getParameter(paramName)));
        return paramMap;
    }
Map<String, String> paramMap = createParamMap(req);
ModelView mv = controller.process(paramMap);
```

-> ModelView는 View의 논리적 이름만 갖고 있으므로 viewResolver에서 viewPath를 반환하여 MyView.render를 실행시켜야 한다.
```
private MyView viewResolver(String viewName) {
	return new MyView("/WEB-INF/views/" + viewName + ".jsp");	//viewName = "new-form"
    }
MyView myView = viewResolver(viewName);
view.render(mv.getModel(), req, res);
```

* V4(단순하고 실용적으로): V3은 잘 설계되었지만 사용하는 개발자 입장에선 번거로운 버전이다. 실용성있게 ModelView를 반환하지 말고 ViewName만 반환하자

-> Http 요청 받음 => URL 매핑 정보에서 컨트롤러를 조회 및 호출(이번엔 paramMap뿐만 아니라 model 객체도 인자로) => ViewName만 반환 => viewName을 viewResolver를 불러서 MyView를 반환받음 => MyView.render(model, req, res)를 호출

-> 기존 버전들은 컨트롤러 인터페이스에서 추상메서드를 ModelView  process(Map<String, String> paramMap)로 만들었지만 viewName만 반환하기 위해서 추상메서드를 String process(Map<String, String> paramMap, Map<String, Object> model)로 선언

-> 기존의 MyView.render를 실행하는 방식은 거의 비슷하지만 model 객체를 새로 생성하여 넘겨주어야 한다.
```
Map<String, Object> model = new HashMap<>();
String viewName = controller.process(paramMap, model);
MyView myView = viewResolver(viewName);
```

* V5(프레임워크를 유연하게 만들기): V3, V4버전을 모두 사용 할 수 있도록 만들자(handlerAdapter)

-> Http 요청 받음 => 핸들러(과거 컨트롤러) 매핑 정보 조회 => 핸들러 어댑터 목록에서 해당 핸들러를 처리할 수 있는 어댑터를 조회 => handlerAdaptor.handle() 실행해서 핸들러를 호출 => ModelView반환
=> 해당 ModelView에서 viewName을 찾아 viewResolver를 호출 => MyView를 반환받고 해당 MyView에서 render실행 => JSP 화면에 뜸

-> 어댑터에서 해당 핸들러가 맞는지 여부와 핸들러를 V3 버전으로 변환 후 실행한다.
 
-> 이전 처럼 핸들러(컨트롤러)만 매핑하는 것이 아니라 어댑터도 리스트에 넣어 초기화 시킨다. 후에 리스트를 순회하면서 해당 핸들러에 맞는 어댑터를 찾는다. 

-> V4의 컨트롤러같은 경우는 String 형식으로 viewName을 반환시키는데 V3의 컨트롤러는 modelView형식으로 반환시킨다. 여기서 어댑터의 역할은 이러한 반환형식의 차이를 없애고 V4 같은 경우에 modelView형식으로 바꿔주는 역할을 한다.

## Spring-MVC

* 위 챕터에서 우리가 만들어본 mvc 프레임워크는 실제 Spring-mvc와 매우 닮아 있다. 

-> Http요청 => 핸들러 매핑정보에서 핸들러를 조회 => 해당 핸들러를 처리 할 수 있는 핸들러 어댑터 목록을 조회 => 조회한 핸들러 어댑터로 handle(handler) 실행해서 handler를 호출 => ModelAndView(또는 String형태의 viewName)을 반환 => viewResolver호출해서 view를 반환
=> render호출 => Html 응답

* 우리가 만든 MVC 프레임워크에서의 frontController의 역할을 DispatcherServlet이 수행한다. (DispatcherServlet의 코드 변경 없이 기능을 변경하거나 확장 가능)

* 옛날에는 Controller 인터페이스를 통해서 구현 but, 요즘에는 @Controller 어노테이션을 통해 구현 (스프링 부트가 뜰 때 HandlerMapping, HandlerAdapter가 실행된다. 둘 모두 @RequestMapping을 찾아서 이용한다.)

* viewResolver를 사용할 때 application.properties에 다음과 같은 코드를 추가해야 논리이름 viewName만 반환하여 사용 할 수 있다.
```
spring.mvc.view.prefix=/WEB-INF/views/
spring.mvc.view.suffix=.jsp
```



