# studySpring

## Spring 동작 방식

* 정적 컨텐츠의 동작 : 해당 요청이 들어오면 스프링 컨테이너가 정적 컨텐츠 관련 컨트롤러를 찾으려함 정적 컨텐츠이므로 컨트롤러가 없어서 resources 내부에 있는 정적 파일을 그대로 화면으로 띄움

* MVC와 템플릿 엔진 : 해다 요청이 들어오면 스프링 컨테이너가 해당 컨트롤러를 찾음 해당 컨트롤러가 반환하는 view를 viewResolver가 찾아서 템플릿 엔진이 처리하여 화면으로 띄움

* API : 어노테이션 @ResponseBody 추가 해당 어노테이션이 추가되면 viewResolver를 사용X 대신 HTTP 응답 방식의 Body에 리턴 값을 직접 반환한다. 보통 객체를 반환한다. -> JSON형식

## 스프링 파일 터미널에서 실행하기

```
$ ./gradlew build 
$ cd /build/libs
$ java -jar [파일이름].jar
```



## 회원 레포 구현 및 테스트 케이스 생성

* 회원 객체 생성(domain) 후 레포 구현 (레포지토리 패키지 -> 멤버 레포 인터페이스 -> 메모리 멤버 레포 클래스 생성)
* 레포에서 쓸 메서드들 구현 (save, findById, findByName, findAll, clearStore) id는 sequence로 하나씩 올려가면서 시스템 자체에서 생성
* 테스트 케이스 생성 -> 테스트파일은 각각의 메서드에 의존관계 없이 생성해야함 -> 따라서 clearStore 만듦 clearStore는 AfterEach로 각각의 메서드가 끝날때맏 실행 -> 기록 지워짐
* 테스트 케이스를 먼저 생성 후 구현하는 방식의 코딩 스타일 (TTD라고 함)

