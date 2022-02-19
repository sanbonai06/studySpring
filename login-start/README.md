# 로그인 구현

## 쿠키를 사용해서 로그인 처리

* 로그인 성공 시 쿠키를 생성해서 Response의 헤더에 담아서 보내준다. -> 웹 브라우저는 해당 쿠키를 지속적으로 유지한다. 

* 영속 쿠키와 세션 쿠키

- 영속 쿠키: 만료 날짜를 입력하면 해당 날짜까지 유지

- 세션 쿠키: 만료 날짜를 생략하면 브라우저 종료시 까지만 유지

쿠키 생성 및 헤더에 담아서 보내주기
```
Cookie idCookie = new Cookie("memberId", String.vlaueOf(loginMember.getId()));
response.addCookie(idCookie);

return "redirect:/";
```

* @CookieValu 어노테이션을 이용하여 쿠키를 조회할 수 있다.

- @CookieValue(name = "", required = "") => 조회 할 쿠키 이름, 필수값 여부등을 인자로 넘길 수 있음

- 로그인 하지 않은 사용자도 접근 할 수 있는 홈 화면이기 때문에 required 값은 false로 넘겨준다.

* 로그아웃 기능

- 세션 쿠키이므로 웹 브라우저 종료 시에 자동으로 쿠키가 사라짐, 서버에서 해당 쿠키의 종료 날짜를 0으로 지정해준다. 
```
cookie.setMaxAge(0);
```

* 우리가 사용한 쿠키는 보안문제가 존재한다.

- 쿠키 값을 임의로 변경 가능하다. (클라이언트에 노출 된 쿠키값을 임의로 변경하여 다른 사용자의 권한을 얻을 수 있음)

- 쿠키에 보관된 정보를 임의의 사용자가 털어 갈 수 있다.

- 해커가 쿠키를 훔치면 평생 사용할 수 있다.

=> 대안: 쿠키에 중요 값을 노출하지 않고 토큰 형식으로 관리하며 토큰의 만료시간을 짧게 유지하는 방식으로 사용 가능하다.

## 세션 동작 방식을 이용해 로그인 처리

* 서버에서 세션을 생성함 -> 세션 id를 uuid방식(추정 불가능한 랜덤 값)으로 생성 -> 생성한 세션 id와 보관 할 값을 매핑하여 서버에서 생성한 세션에 저장 

=> 결론은 회원과 관련된 중요 정보들은 클라이언트단에 전달되지 않는다. (보안 강화)

HttpSession 사용
```
HttpSession session = request.getSession();
session.setAttribute(세션 이름, 값);
```

* request.getSession(true); : create옵션이 true이면 세션이 존재하지 않을 때 새로운 세션을 생성해 반환
반대로 false면 null을 반환 

세션 종료
```
HttpSession session = reqest.getSession(false);
if (session != null){
	session.invalidate();
}
```

세션 조회
```
HttpSession session = request.getSession(false);
session.getAttribute(세션 이름);
```

간편한 방법
```
public String homeLoginSpring(@SessionAttribute(name=세션이름, required = false) Member loginMember)
{}
```

세션을 찾고 세션 내부의 데이터들을 찾는 과정을 스프링이 자동화 해줌 

* 세션 타임아웃 설정(스프링 부트로 글로벌 설정)

```
//application.propereties
server.servlet.session.timeout=1800 //글로벌 설정은 분단위 ex.1800=30분
```

=> 마지막 접근 시간을 기준으로 설정해둔 timeout이 지나면 was가 해당 세션을 제거함

* 주의: 실무에서 세션에는 최소한의 데이터만 보관하자(사용자 수 * 저장할 데이터의 용랑 때문에 메모리 사용량이 급격히 늘어나서 장애로 이어질 수 있음)
