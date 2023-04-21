# 블로그 프로젝트
## 프로젝트
1. 프로젝트 구축 단계
   1. 1단계 (핵심로직부터, 유저, 상품, 채팅로그)
      - 회원가입 (유효성검사, 중복체크, AOP 중요하지 않음)
      - 로그인
      - 상품등록 (내용, 사진X - 기본값 더미)
      - 상품목록 (유저정보, 상품정보, 거리순, 인기순 X)
      - 상품 상세보기 (글, 버튼(채팅))
      - 상대방과 채팅 -Push서버, 웹소켓
   2. 2단계
      - 상품등록 사진 등록 -파일 기술
      - 상품목록 거리순 (GPS) - GPS(정보 더미데이터) 기술 (위경도 - 동네정보)

## 1. 기술스택
- JDK 11
- Springboot 2.X 버전
- JPA
- H2 인메모리 디비 - 방언 MySQL
- JSP
- Security
- AJAX
- JSoup
- 부트스트랩
## 2. 요구사항
> HTTP 메서드를 POST와 GET만 사용한다.
#### 요구사항 1단계
- 회원가입
- 로그인 (시큐리티)
- 글쓰기 (섬머노트)
- 게시글 목록보기 (LAZY 전략 - N+1 해결 방법)
- 게시글 상세보기 (데이터 출력)
- 썸네일 등록하기 (Jsoup API 이용)
#### 요구사항 2단계
- 아이디 중복확인 (중복확인후 아이디 변경시 다시 중복체크 요구)
- 비밀번호 동일체크
- 로그인 아이디 기억하기
- 프론트 유효성 검사 (onsubmit)
- 백엔드 유효성 검사 (AOP 등록)
- 글로벌 Exception 처리
- Script 응답 설정
#### 요구사항 3단계
- 회원 프로필 사진 등록
- 회원정보 보기
- 회원정보 수정
- 에러 로그 테이블 관리
#### 요구사항 4단계
- 페이징
- 검색
#### 요구사항 5단계
- Love 테이블 생성
- Reply 테이블 생성
- 연관관계 설정
- 댓글쓰기
- 댓글삭제
- 좋아요 하기
- 좋아요 보기
- 남은 기능 추가하기
-  보완하고 싶은것 추가하기


#### Day 1
1. yml 설정
2. package 설정
   - board
   - love
   - reply
   - user
3. SecurityConfig
   - 인증 및 권한 설정 : /s/**은 모두 인증 필요한 주소 설정
4. 화면 설정
   - web-inf은 접근 불가능
   - /webapp은 기본설정 주소 webapp 주소 이후로 적으면 접근가능
   - viewresolver로 주소 설정
   ```yaml
   spring:
     mvc:
      view:
       prefix: /WEB-INF/views/
       suffix: .jsp
   ```
   - header와 footer 설정 (Template Engine 기능)
     - 결제시스템에서는 footer에 사업자주소 필요 -> 승인

### Day 2
#### URI 코드 컨벤션
 //URI 컨벤션
 // 인증이 되지 않은 상태에서 인증과 관련된 주소는 엔티티를 적지 않는다.
 // 행위: /리소스/식별자
 // POST 메서드로 DELETE, UPDATE, INSERT 모두 사용
 //write (post) : /리소스/식별자/(pk, uk) save or delete or update
 //read  (get)  : /리소스/식별자

### 대주제
1. 회원가입
2. 로그인 (시큐리티 구현)

### 소주제
1. 회원가입
   1. API 패키지 뷰테스트 패키지 작성
   2. Join 핸들러 메서드 작성
   3. 엔티티 -> Repository -> DTO 작성
   4. Contorller의 역할은 요청의 유효성 검사하고 Service에 요청을 전달하는 것
   5. 동일한 유저네임으로 회원가입시
      - JPA가 만든 메서드 에러 발생시 -> 제어권을 JPA가 가지고 있다.
      - save함수는 JPA가 작성 -> save에는 throw가 없음 -> try-catch문으로 직접 제어
      - save 내부에서 에러 터짐 - DataIntegrationViolationException 발동
      - 컨트롤러로 처리하지 않고, save 내부에서 터진 서비스의 책임으로 직접 처리해야한다. -> throw로 던지지 못한다.
   6. 더미 데이터 작성
   ```java
       //개발중에서만 사용하는 어노테이션
       @Profile("dev")
       //화면 테스트시 회원가입하는 과정을 하지 않기 위해서
       @Bean
       CommandLineRunner init(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
           return args -> {
               User ssar = User.builder()
                       .username("ssar")
                       .password(passwordEncoder.encode("1234"))
                       .email("ssar@nate.com")
                       .role("USER")
                       .profile("person.png")
                       .status(true)
                       .build();
               userRepository.save(ssar);
           };
       }
   ```
   7. H2 콘솔 보안 해제 
      - frameOption 해제
      - http.headers().frameOptions().disable();
   8. joinForm 유효성 검사 잠시 해제하고 화면 개발
      - 이메일 형식 유효성검사는 살아있다.
      - 중복 확인, 유효성검사 추후에 개발
2. 로그인
   1.  @NoArgsConstructor(access = AccessLevel.PROTECTED) 
   2. UserDetailsService, UserDetails 작성
   2. UserDetailsService를 커스터마이징 해야함
   3. 회원을 찾지 못했을때 UsernameNotFoundException 던지도록 한다.
   4. loginProcessingUrl로 인해 MyUserDetaislsService 호출 (post, x-www-form-urlEncoded)
   5. 뷰에서 action에 /login 페이지 남겨서 타도록 설정
   5. 세션 값 확인 필요
## CDN로 썸머노트 추가 
1. 클라이언트에서 다운로드하므로 굳이 다운로드를 피하기 위해서 CDN 사용
2. 먼저 테스트 수행해야 한다. -> 코드를 풀로 테스트
3. jQuery로 작성된 썸머노트
3. $(document).ready() 화면에 로딩 됐을때 -> 바디 위에 있으므로 필요 없음
4. 자바스크립트는 항상 화면의 가장 밑에
5. #은 id를 찾는다. .은 클래스를 찾는다.
6. textarea에 적용한다. -> class만 summernote
7. 길이가 길었으면, -> 중괄호로 존재하면 -> 자바스크립트의 오브젝트
   ```java
   <script>
    $('.summernote').summernote({
        tabsize: 2,
        height: 500
    });
   </script>
   ```
## 썸머노트 추가한 saveForm을 호출하는 글쓰기
1. 엔티티 Board 작성 - @Lob, @NoArgs~(AccessLevel.PROTECTED)
2. 로그인시 메뉴 바뀌도록, 시큐리티가 세션을 관리하고 있으므로 JSP Security Tag Library -> 탬플릿 엔진에 따라 다르므로 비추
3. SecurityContextHolder에 접근 불가능한 EL 표현식
4. SecurityConfig에서 로그인 성공시 세션에 추가
```java
//로그인 성공시 메뉴 변경을 위해 -> View에서 접근하려고
MyUserDetails myUserDetails=(MyUserDetails) authentication.getPrincipal();
//principal에 Userdetails가 들어간다. -> 캐스팅 필요
HttpSession session = request.getSession();
session.setAttribute("sessionUser", myUserDetails.getUser());
```
5. SecurityContextHolder의 세션과 나머지 세션영역은 분리된 영역이지만 JSESSIONID로 둘다 접근 가능 (뷰에서만 세션에서 가져옴)
6. 로그아웃에서는 모든 세션이 날라간다 sessioninvalidate
7. 주의할 사항은 유저 정보변경시 직접 동기화 필요
8. 썸머노트로 글쓰기시 HTML태그가 들어간다.
9. 이미지 넣을경우 mime타입과 base64로 인코딩해서 들어간다. (바이너리 데이터 -> 문자열 데이터)
10. 나중에 썸네일 만들때 사용한다.

## 코드 설계시 고민한 점
1. DTO에서 암호화 하는 것과 서비스에서 암호화 하는 것 중 어느 것이 더 좋은 코드일까?
DTO에서 암호화를 수행하는 경우, 암호화된 비밀번호가 데이터베이스에 저장되므로, 서비스 레이어에서 별도의 암호화 작업이 필요하지 않습니다.
이는 보안상의 이점을 가질 수 있으며, 클라이언트에서 서버로 전송되는 데이터가 암호화되어 있으므로, 중간자 공격을 방지할 수 있습니다. 
하지만 DTO가 다른 레이어와 독립적으로 동작하는 것이 이상적이며, DTO가 너무 많은 역할을 하게 될 경우, 유지 보수가 어려워질 수 있습니다.
반면에 서비스에서 암호화를 수행하는 경우, 서비스 레이어에서 비즈니스 로직과 분리되어 암호화와 관련된 모든 처리를 담당합니다.
이는 서비스 레이어의 책임을 명확히 할 수 있으며, DTO가 더욱 간결해질 수 있습니다. 하지만 클라이언트에서 서버로 전송되는 데이터가 암호화되지 않으므로, 중간자 공격에 취약할 수 있습니다.
따라서, 상황에 따라 적절한 방법을 선택하는 것이 좋습니다. 보통은 서비스 레이어에서 암호화를 수행하는 것이 좋은 방법입니다. 
그러나 클라이언트와 서버 간의 통신이 매우 중요한 경우에는 DTO에서 암호화를 수행하는 것이 더 나은 방법일 수 있습니다.

2. 세션에는 User정보가 있는데 디비에 User정보가 없을 수도 있을까? 팬텀 객체?
세션에는 User 정보가 있을 수 있지만, 디비(Database)에는 해당 User 정보가 없을 수 있습니다. 이는 팬텀 객체(Phantom Object)와 관련이 있습니다.
팬텀 객체란, ORM(Object-Relational Mapping) 기술을 사용할 때, 데이터베이스와의 동기화가 이루어지지 않은 객체를 의미합니다. 즉, 데이터베이스에는 해당 객체가 존재하지 않는데도, 해당 객체를 메모리에 불러와 사용할 때 발생할 수 있는 문제를 말합니다.
예를 들어, 세션에 저장된 User 정보는 이전에 로그인한 사용자의 정보일 수 있습니다. 
그러나 해당 사용자가 데이터베이스에서 삭제된 경우, 세션에는 여전히 사용자 정보가 남아 있을 수 있습니다. 이 경우, 해당 User 객체는 팬텀 객체가 됩니다. 따라서 이 객체를 사용하면 데이터베이스와 동기화되지 않은 정보를 사용하게 됩니다.
따라서 ORM을 사용할 때는 이러한 팬텀 객체에 대한 처리를 고려해야 합니다. 일반적으로는 데이터베이스에 존재하지 않는 객체를 사용할 때 예외 처리를 하거나, 해당 객체가 삭제되었음을 알리는 메시지를 출력하는 등의 방법으로 처리합니다.

