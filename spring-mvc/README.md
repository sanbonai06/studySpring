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
