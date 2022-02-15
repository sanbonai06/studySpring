# 메시지, 국제화

## 메시지

* 반복되는 텍스트들 (ex. item-service에서 상품등록, 상품수정 등등)을 바꿀 때 편리하게 텍스트 대치로 변환 가능하게 해주는 기능

* application.properties파일에 spring.messages.basename=messages로 메시지파일 이름 등록 =>  resources 파일 밑에 messages.properties 파일 생성 => 메시지 정보 등록(ex.item.itemName=상품 이름) 

* MessageSource 선언 후 접근 가능 ms.getMessage메서드 접근

* hello.name=hello {0}로 선언 시 파라미터 넘기기 가능(getMessage() 메서드에서 new Object[]("Spring!")식으로 객체 배열을 넘겨야함

* 타임리프에서는 #{...}으로 사용

## 국제화

* 메시지 파일 등록 시 messages_en.properties와 같이 이름 설정

* 크롬에서 접속할 때, 언어를 영어로 바꿔주면 헤더에 언어 정보 넘어올 때 메시지 파일을 messages_en.properties로 읽어들임 
