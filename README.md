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
- 게시글 목록보기과 페이징 (LAZY 전략 - N+1 해결 방법)
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
3. 글쓰기
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

4. 글 목록 보기
   1. 서비스에 readOnly 설정 - 변경을 무시하고 읽기는 가능하도록
   ```java
    @Transactional(readOnly = true) 
    //변경감지 하지않기 위해, 고립성을 지키기 위해 (repeatable read)
   ```
   2. 서비스에서 반환된 Page<Board>는 사실상 비영속 상태 (OSIV 비활성화 상태이므로 세션 종료)
   3. 더미데이터 추가 -> 직접 보기 위해 만들었을 때 User를 Lazy Loading 불가능해서 터짐
   ```java
   @Bean
    CommandLineRunner init(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, BoardRepository boardRepository) {
        return args -> {
            User ssar = User.builder()
                    .username("ssar")
                    .password(passwordEncoder.encode("1234"))
                    .email("ssar@nate.com")
                    .role("USER")
                    .profile("person.png")
                    .build();
            User cos = User.builder()
                    .username("cos")
                    .password(passwordEncoder.encode("1234"))
                    .email("cos@nate.com")
                    .role("USER")
                    .profile("person.png")
                    .build();
            userRepository.saveAll(Arrays.asList(ssar, cos));

            Board b1 = Board.builder()
                    .title("제목1")
                    .content("내용1")
                    .user(ssar)
                    .thumbnail("/upload/person.png")
                    .build();
            Board b2 = Board.builder()
                    .title("제목2")
                    .content("내용2")
                    .user(cos)
                    .thumbnail("/upload/person.png")
                    .build();
            boardRepository.saveAll(Arrays.asList(b1, b2));
        };
    }
   ```


5. Page 객체 구조
```json
{
  "content": [
    {
      "id": 1,
      "user": {
        "id": 1,
        "username": "ssar",
        "password": "$2a$10$O5/W0XuCW9EnQyK0XBT4TOos74I5prcSMTPx0t1XvTUN5WBJBK/zC",
        "email": "ssar@nate.com",
        "role": "USER",
        "profile": "person.png",
        "status": null,
        "createdAt": "2023-04-21T21:55:12.401086",
        "updatedAt": null
      },
      "title": "제목1",
      "content": "내용1",
      "thumbnail": "/upload/person.png",
      "createdAt": "2023-04-21T21:55:12.439085",
      "updatedAt": null
    },
    {
      "id": 2,
      "user": {
        "id": 2,
        "username": "cos",
        "password": "$2a$10$qh72dfNLKk4bAIMlYvypHuRvA0z66TnCsV4GbTFchOutFa4dppcyS",
        "email": "cos@nate.com",
        "role": "USER",
        "profile": "person.png",
        "status": null,
        "createdAt": "2023-04-21T21:55:12.431084",
        "updatedAt": null
      },
      "title": "제목2",
      "content": "내용2",
      "thumbnail": "/upload/person.png",
      "createdAt": "2023-04-21T21:55:12.443085",
      "updatedAt": null
    }
  ],
  "pageable": {
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 0,
    "pageSize": 8,
    "pageNumber": 0,
    "unpaged": false,
    "paged": true
  },
  "last": true,
  "totalElements": 2,
  "totalPages": 1,
  "size": 8,
  "number": 0,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "first": true,
  "numberOfElements": 2,
  "empty": false
}


```

#### Day 3

1. Page 객체 뿌리기
   - content
   - pageSize
   - last
   - first
   - number
   - totalPage

3. 화면단
    6. 삼항 연사자 사용해서 구현
    - <li class="page-item ${boardPG.first ? "disabled" : ""}"><a class="page-link" href="/?page=${boardPG.number -1}">Previous</a></li>
    - <li class="page-item ${boardPG.last ? "disabled" : ""}"><a class="page-link" href="/?page=${boardPG.number +1}">Next</a></li>
    - 현재 페이지를 이용해 이전, 다음 페이지를 구한다. 
    - previous와 Next 비활성화를 담당한다.

4. findAll()
   - 리포지토리 테스트
5. 실무적인 쿼리
   1. @ManyToOne만 사용
   2. 모든 것은 Lazy 전략
   3. Repo -> Test 쿼리 확인 (N+1 발생시) -> fetch Join
      1. 데이터의 크기에 따라 다름
      2. 적은 유저가 많은 데이터 작성 시엔 inQuery
      3. 그 외엔 모두 fetch join
      4. Page 직접 만든 이유 findAll 사용시 N+1발동하기 떄문에
   5. OSIV는 False
   6. 서비스에서 응답 DTO 작성 -> 컨트롤러에게 전달
   7. 단방향 매핑시 -> User, Account, Transaction 모두 보고 싶을 때
      1. select Account -> Join Fetch User (one 방향은 페치조인)
      2. select Transaction -> 총 2번 조회
      3. 즉 DTO(Account, TransactinList)으로 만들어서 스트림으로 처리
6. 예외처리와 에러로그 구현
   - ssr의 경우에는 자바스크립트로 리턴
   - csr의 경우 DTO로 리턴하는데
     - ssr은 두개로 나누어서 만들어둬야한다.
     - 실제 화면단에서는 자바스크립트 알림으로 에러를 응답해야하므로
7. EL표현식으로 화면에 렌더링

#### Day 4
- summernote는 png만 지원한다. -> jpg일 경우 null 터짐
1. 프로필 사진 추가 
2. 

#### 프로필 사진 추가
1. 헤더의 로그인 사용자 정보 변경
   - 기본 프로필 사진으로 설정
   - 경로는 /upload/profile.png
   - 하지만, 실행환경에서는 static폴더에서 찾기 때문에, /static/upload 폴더는 존재하지 않는다.
   - 리소스 핸들러를 이용해 해당 위치를 찾아갈 수 있도록 WebMvcConfig 설정 클래스를 작성한다.
   - WebMvcConfig 클래스를 구현해서 할 수 있는 대표적인 설정
     1. 인터셉터 등록: addInterceptors 메소드를 구현하여 인터셉터를 등록할 수 있습니다. 예를 들어, 인증 정보가 있는지 확인하거나 권한 검사를 수행하는 인터셉터를 등록할 수 있습니다.
     2. 리소스 핸들러 등록: addResourceHandlers 메소드를 구현하여 정적 리소스를 처리하는 핸들러를 등록할 수 있습니다. 예를 들어, CSS, JavaScript 및 이미지 파일과 같은 정적 파일을 처리하는 핸들러를 등록할 수 있습니다. 
     3. 비밀번호 인코더 설정: configurePathMatchers 메소드를 구현하여 비밀번호 인코더를 설정할 수 있습니다. 예를 들어, BCryptPasswordEncoder를 사용하여 비밀번호를 인코딩하도록 설정할 수 있습니다. 
     4. 예외 처리기 등록: configureHandlerExceptionResolvers 메소드를 구현하여 예외 처리기를 등록할 수 있습니다. 예를 들어, 사용자가 권한이 없는 페이지에 액세스하려고 시도 할 때 보여줄 예외 처리기를 등록할 수 있습니다.
     5. 로그인 페이지 설정: configureDefaultServletHandling 메소드를 구현하여 로그인 페이지를 설정할 수 있습니다. 예를 들어, 로그인 폼을 사용자 지정 JSP 페이지로 구성할 수 있습니다. 
     6. 보안 HTTP 헤더 설정: configureContentNegotiation 메소드를 구현하여 보안 HTTP 헤더를 설정할 수 있습니다. 예를 들어, X-Frame-Options, X-XSS-Protection 및 X-Content-Type-Options와 같은 헤더를 설정할 수 있습니다.
2. WebMvcConfig에 리소스 핸들러 등록
```java
package com.mino.blogproj.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // CORS
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        // 1. 절대경로 file:///c:/upload/
        // 2. 상대경로 file:./upload/
        registry
                .addResourceHandler("/upload/**")
                .addResourceLocations("file:" + "./upload/")
                .setCachePeriod(60 * 60) // 초 단위 => 한시간
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}
```
- @Configuration 어노테이션은 해당 클래스가 스프링 설정 클래스임을 나타냅니다.
- WebMvcConfigurer 인터페이스를 구현하여 addResourceHandlers 메소드를 구현합니다. 이 메소드는 정적 파일 요청을 처리할 때 사용됩니다. 
- registry.addResourceHandler("/upload/**")는 "/upload"로 시작하는 요청을 처리하기 위해 Handler를 등록합니다. 
- addResourceLocations 메소드는 실제 파일이 저장된 경로를 설정합니다. 여기서는 "./upload/" 폴더를 사용합니다. 
- setCachePeriod 메소드는 클라이언트 캐시 지속 시간을 설정합니다. 이 경우, 1시간(60*60) 동안 클라이언트 캐시가 유지됩니다. 
- resourceChain 메소드는 리소스 체인을 활성화합니다. 이 옵션을 활성화하면, 정적 파일 요청이 들어왔을 때 다양한 리소스 변형과 미리 캐싱된 리소스를 적용하는 것이 가능합니다. 
- addResolver 메소드는 정적 리소스 경로에 대한 정보를 해석하는 리졸버를 등록합니다. PathResourceResolver는 URI를 파일 경로로 변환하는 데 사용됩니다.
3. 각각 로그인한 사용자의 정보를 가져오기 위해서 저장해둔 세션의 sessionUser 데이터를 가져온다.
```html
<a href="/s/user/${sessionUser.id}/updateProfileForm"><img src="/upload/person.png" style="width: 35px;"
                                                       class="rounded-circle" alt="Cinque Terre"></a>
```
   - 템플릿 엔진에서 EL표현식으로 로그인한 사용자정보를 사용하기 위해서 로그인 완료시 sessionUser정보를 저장해놨다.
   - 왜냐하면 화면단에서는 securityContextHolder에 접근을 못하고 Page, Request, Session, Application만 접근 가능
4. 사진 파일인지 체크하고, 사진 업로드가 완료되면 사진을 보여주고 완료 클릭시, 업로드된 사진으로 변경을 form에 요청하는 자바스크립트
```javascript
    function chooseImage(obj){
        //console.log(obj);
        //console.log(obj.files);
        let f  = obj.files[0];
        if(!f.type.match("image.*")){
            alert("이미지를 등록해야 합니다.");
            return;
        }
        let reader = new FileReader();
        reader.readAsDataURL(f);
        // 콜스택이 다 비워지고, 이벤트 루프로 가서 readAsDataURL 이벤트가 끝나면 콜백시켜주는 함수
        reader.onload = function (e){
            console.log(e);
            console.log(e.target.result);
            $("#imagePreview").attr("src", e.target.result);
        }
    }
```
5. 자바스크립트로부터 받은 사진 변경 요청을 처리하는 form
```html
<div class="container my-3">
    <h2 class="text-center">프로필 사진 변경 페이지</h2>
<%--  모델에 user 정보를 넘겨줘야하고, enctype은 사진 전송 타입인 multipart로 보낸다.--%>
    <form action="/s/user/${user.id}/updateProfile" method="post" enctype="multipart/form-data">
        <div class="form-group">
<%--            <img src="/images/dora.png" alt="Current Photo" class="img-fluid" id="imagePreview">--%>
            <img src="/upload/${user.profile}" alt="Current Photo" class="img-fluid" id="imagePreview">
<%--            리졸브 핸들러가 /upload 경로를 변경해준다.--%>
        </div>
        <div class="form-group">
            <input type="file" class="form-control" id="profile" name="profile" onchange="chooseImage(this)">
        </div>
        <button type="submit" class="btn btn-primary">사진변경</button>
    </form>
</div>
```
6. 유저 정보를 DB에서 조회해서 모델에 전달한다.
```java
        User userPS = userService.회원프로필보기(id);
        model.addAttribute("user", userPS);
        return "user/profileUpdateForm";
```
7. 프로필 사진 업데이트하는 핸들러 메서드 작성 - 수정한 사진 등록 후에는, 반드시 세션 동기화 필수 
```java
	@PostMapping("/s/user/{id}/updateProfile")
	    public String profileUpdate(
	            @PathVariable Long id,
	            MultipartFile profile,
	            @AuthenticationPrincipal MyUserDetails myUserDetails
	            ){
	        // 1. 권한 체크
	        if(id != myUserDetails.getUser().getId()){
	            throw new Exception403("권한이 없습니다");
	        }
	
	        // 2. 사진 파일 유효성 검사
	        if(profile.isEmpty()){
	            throw new Exception400("profile", "사진이 전송되지 않았습니다");
	        }
	
	        // 3. 사진을 파일에 저장하고 그 경로를 DB에 저장
	        User userPS = userService.프로필사진수정(profile, id);
	
	        // 4. 세션 동기화
	        myUserDetails.setUser(userPS);
	        session.setAttribute("sessionUser", userPS);
	
	        return "redirect:/";
	    }
```
- multipart/form-data로 보낸 file은 MultipartFile 타입이 된다.
- 사진을 Base64로 변환해서 DB에 저장하지 않고, 프로필 사진을 파일에 저장하고 그 경로를 DB에 저장한다.
8. 프로필 사진 수정하는 메서드
```java
	@Transactional
	    public User 프로필사진수정(MultipartFile profile, Long id) {
	        try {
	            String uuidImageName = MyFileUtil.write(uploadFolder, profile);
	            User userPS = userRepository.findById(id)
	                    .orElseThrow(()->new Exception500("로그인 된 유저가 DB에 존재하지 않음"));
	            userPS.changeProfile(uuidImageName);
	            return userPS;
	        }catch (Exception e){
	            throw new Exception500("프로필 사진 등록 실패 : "+e.getMessage());
	        }
	    }
```
9. 파일을 저장하는 유틸 클래스 작성 - 파일과 파일 저장 경로를 파라미터로 받는다.
```java
    @Value("${file.path}")
    private String uploadFolder;

	public static String write(String uploadFolder, MultipartFile file) {
	        UUID uuid = UUID.randomUUID();
	        String originalFilename = file.getOriginalFilename();
	        String uuidFilename = uuid + "_" + originalFilename;
	        try {
	            Path filePath = Paths.get(uploadFolder + uuidFilename);
	            Files.write(filePath, file.getBytes());
	        } catch (Exception e) {
	            throw new Exception500("파일 업로드 실패 : "+e.getMessage());
	        }
	        return uuidFilename;
	    }
```
- 파일 저장시에 이름 충돌을 방지하기 위해 해당 시간+랜덤값을 전달하는 롤링 기법 사용
- 유효아이디(UUID) 사용해서 이름 충돌을 방지한다.
- DB에는 사진의 이름만 저장하고, 서버에 파일을 저장
10. 프로퍼티 설정에 최대 파일 크기 설정
```yaml
	servlet:
	    multipart:
	      max-file-size: 10MB
	      enabled: true
```



## 개발환경과 실행환경
1. 프로젝트 작업환경에서 deploy를 실행하면 실행파일이 생성 (jar/war)
2. 해당 실행파일을 그대로 실서버에 배포할 수는 없다. (개발환경과 실행환경이 다르기 때문에)
3. github을 이용해 CI를 수행한다. (지속적인 통합)
4. 배포를 하기전에, 테스트 서버에서 테스트를 수행하고, 빌드한다.
   1. OS 설치
   2. JDK 설치
   3. Junit 테스트
   4. Build
5. OS마다 경로의 차이
   1. 윈도우의 경로 : C:\Users\user\Documents\example.txt
   2. 리눅스/맥의 경로 : /home/user/Documents/example.txt
      - 리눅스와 맥은 Unix 계열의 운영체제이므로 경로 구분자로 슬래시 (/)를 사용합니다. 반면, 윈도우는 DOS 계열의 운영체제이므로 역슬래시()를 사용합니다. 
      - 윈도우에서는 드라이브 이름이 경로에 포함되어 있어, 드라이브의 루트 디렉토리로부터의 상대 경로를 사용할 수도 있습니다.
6. 따라서 개발 환경, 테스트 환경, 개발 환경일 때 각각 사용할 경로를 프로퍼티로 설정해준다.
```yaml
file:
  path: ./upload/
```

#### 프론트 유효성 검사
- 프론트, 컨트롤러에서 검사를 하지 않으면, DB에서 에러가 발생
- 프론트와 컨트롤러의 책임을 다하지 않았기 때문이다.
1. 필드에 maxlength, required 설정
2. onSubmit은 회원가입 버튼이 클릭이 되면, action이 아니라 유효성 검사가 실행되고, true일 때 action 발동
3. 비밀번호와 비밀번호 확인이 같아야지 회원가입이 되도록 설정
4. 사용자이름 중복체크를 하지 않으면, 중복체크해달라고 요구 -> 기본값이 false
   1. onchange : 입력 필드에서 값이 변경되어 포커스가 해제되었을 때 발생
      - 변경될 때마다 발생하도록 해야 중복체크를 하고, 유저네임을 바꾸는 일을 방지할 수 있다.
   2. oninput  : 입력 필드에서 값이 변경될 때마다 발생
5. 중복체크하는 sameUsername 메서드는 비동기 함수로, DB에 조회를 수행한다. -> ajax fetch요청
   1. api 요청을 통해 응답코드 200을 받으면 성공 -> innerHTML로 중복체크되지 않았다는 내용을 지우고, true 처리
   2. 다른 응답코드를 받으면 사용할 수 없다고 실패
6. JSON을 리턴받는 UserApiController 작성

#### 아이디 중복확인
1. 

## N+1 해결방법
1) 새로운 리포지토리에 필요할때마다 Join fetch로 만들어서 사용한다. -이너 조인이 발생
2) @EntityGraph 이용 - leftOuterJoin이 발생하므로 좋지는 않다.
3) batch size 설정

여러가지 방법의 장단점
: Join fetch, @EntityGraph, batch size는 모두 Hibernate의 성능 최적화를 위한 기능으로, 각각의 특징과 적용 방법을 고려하여 사용해야 합니다.

1. Join fetch
- Join fetch는 JPQL에서 사용하는 기능으로, 연관된 엔티티를 한 번에 조회할 수 있도록 지원합니다.
- 이를 통해 n+1 문제를 해결할 수 있으며, 연관된 엔티티를 지연로딩하지 않고 즉시 로딩하여 조회합니다
    - 사용하기 쉽고 간단하게 적용할 수 있습니다.
    - 쿼리 결과를 즉시 로딩하기 때문에, 연관된 엔티티 수가 많을 경우 메모리 부족이 발생할 수 있습니다.
    - 복잡한 쿼리를 작성할 경우 성능이 저하될 수 있습니다.
    - 따라서, Join fetch는 간단한 쿼리에서 적용하기 적합하며, 연관된 엔티티 수가 적을 때 성능 최적화에 효과적입니다.

2. @EntityGraph
- @EntityGraph는 Hibernate 5.1 이상부터 지원하는 기능으로, 연관된 엔티티를 즉시 로딩하면서도, 메모리를 효율적으로 사용할 수 있도록 지원합니다.
    - 연관된 엔티티를 즉시 로딩하면서도 메모리를 효율적으로 사용할 수 있습니다.
    - 복잡한 쿼리에서도 성능이 유지됩니다.
    - @EntityGraph를 적용할 때, join fetch보다 더욱 세밀한 조정이 가능합니다.
    - 하지만, @EntityGraph는 애플리케이션에서 사용하는 엔티티에 대해 개발자가 직접 정의해주어야 하기 때문에, 사용하기에는 조금 더 복잡합니다.

3. Batch size
- Batch size는 Hibernate의 Batch Fetching 기능 중 하나로, 여러 엔티티를 한 번에 가져와 메모리 소비를 줄이고 성능을 향상시키는 방법입니다.
    - 여러 엔티티를 한 번에 조회하여 메모리 소비를 줄일 수 있습니다.
    - Join fetch와 달리, 복잡한 쿼리에서도 성능이 유지됩니다.
    - Batch size를 적용하려면, 엔티티 클래스의 @OneToMany 또는 @ManyToMany 어노테이션에 @BatchSize 어노테이션을 추가하여 사용합니다.
    - Batch size는 간단한 쿼리에서 사용하기 적합하며, 연관된 엔티티 수가 많을 때 성능 최적화에 효과적입니다.
    - 하지만 Batch size는 연관된 엔티티를 한 번에 조회하므로, 엔티티의 수가 많으면 메모리 사용량이 늘어나 성능에 오히려 악영향을 미칠 수 있습니다.


## 코드 설계시 고민한 점
1. DTO에서 암호화 하는 것과 서비스에서 암호화 하는 것 중 어느 것이 더 좋은 코드일까?
- DTO에서 암호화를 수행하는 경우, 암호화된 비밀번호가 데이터베이스에 저장되므로, 서비스 레이어에서 별도의 암호화 작업이 필요하지 않습니다.
- 이는 보안상의 이점을 가질 수 있으며, 클라이언트에서 서버로 전송되는 데이터가 암호화되어 있으므로, 중간자 공격을 방지할 수 있습니다.
- 하지만 DTO가 다른 레이어와 독립적으로 동작하는 것이 이상적이며, DTO가 너무 많은 역할을 하게 될 경우, 유지 보수가 어려워질 수 있습니다.
- 반면에 서비스에서 암호화를 수행하는 경우, 서비스 레이어에서 비즈니스 로직과 분리되어 암호화와 관련된 모든 처리를 담당합니다.
- 이는 서비스 레이어의 책임을 명확히 할 수 있으며, DTO가 더욱 간결해질 수 있습니다. 하지만 클라이언트에서 서버로 전송되는 데이터가 암호화되지 않으므로, 중간자 공격에 취약할 수 있습니다.
- 따라서, 상황에 따라 적절한 방법을 선택하는 것이 좋습니다. 보통은 서비스 레이어에서 암호화를 수행하는 것이 좋은 방법입니다.
- 그러나 클라이언트와 서버 간의 통신이 매우 중요한 경우에는 DTO에서 암호화를 수행하는 것이 더 나은 방법일 수 있습니다.

2. 세션에는 User정보가 있는데 디비에 User정보가 없을 수도 있을까?
- 세션에는 User 정보가 있을 수 있지만, 디비(Database)에는 해당 User 정보가 없을 수 있습니다. 이는 팬텀 객체(Phantom Object)와 관련이 있습니다.
    - 팬텀 객체란, ORM(Object-Relational Mapping) 기술을 사용할 때, 데이터베이스와의 동기화가 이루어지지 않은 객체를 의미합니다.
    - 즉, 데이터베이스에는 해당 객체가 존재하지 않는데도, 해당 객체를 메모리에 불러와 사용할 때 발생할 수 있는 문제를 말합니다.
    - 예를 들어, 세션에 저장된 User 정보는 이전에 로그인한 사용자의 정보일 수 있습니다.
    - 그러나 해당 사용자가 데이터베이스에서 삭제된 경우, 세션에는 여전히 사용자 정보가 남아 있을 수 있습니다.
    - 이 경우, 해당 User 객체는 팬텀 객체가 됩니다. 따라서 이 객체를 사용하면 데이터베이스와 동기화되지 않은 정보를 사용하게 됩니다.
- 따라서 ORM을 사용할 때는 이러한 팬텀 객체에 대한 처리를 고려해야 합니다.
- 일반적으로는 데이터베이스에 존재하지 않는 객체를 사용할 때 예외 처리를 하거나, 해당 객체가 삭제되었음을 알리는 메시지를 출력하는 등의 방법으로 처리합니다.

3. @PageDefault, Pageable과 차이가 뭘까
- @PageDefault와 Pageable은 모두 스프링 프레임워크에서 제공하는 페이징 처리 기능을 위한 인터페이스 및 어노테이션입니다.
- @PageDefault는 페이징 처리 시 기본 값 설정을 위한 어노테이션으로, 페이지 크기(page size)와 페이지 번호(page number)를 기본값으로 설정할 수 있습니다.
    - 이를 사용하여 기본값을 설정하면, 컨트롤러에서 페이징 처리 시 해당 어노테이션으로 설정한 값이 적용됩니다.
- 반면, Pageable은 스프링에서 제공하는 페이징 처리를 위한 인터페이스로, 페이징 정보를 포함하는 객체입니다.
    - Pageable 객체를 사용하면 페이징 처리 시 페이지 번호, 페이지 크기, 정렬 조건 등 다양한 정보를 설정할 수 있습니다.
    - 어떤 방식이 더 좋은지는 사용하는 상황에 따라 다릅니다. @PageDefault를 사용하면 컨트롤러에서 간단하게 기본값을 설정할 수 있어 편리합니다.
    - 하지만 Pageable 객체를 사용하면 페이지 크기, 페이지 번호, 정렬 조건 등 다양한 정보를 설정할 수 있어 더욱 유연한 페이징 처리가 가능합니다.
- 따라서, 페이징 처리의 복잡도가 높을 경우 Pageable 객체를 직접 만들어서 사용하는 것이 좋습니다.
- 그러나 간단한 페이징 처리에서는 @PageDefault를 사용하는 것이 더 효율적일 수 있습니다.

4. open-in-view를 사용할 때와 사용하지 않을 때 어떤 차이가 있을까
- OSIV(Open Session In View)을 사용하면 HTTP 요청과 응답 사이에 영속성 컨텍스트를 유지할 수 있으므로, View Template에서 지연로딩 엔티티에 접근하거나 세션 상태를 유지해야 하는 경우에 유용합니다.
- 세션 유지나 지연로딩 엔티티에 접근이 필요하지 않은 경우에는 OSIV를 사용하지 않는 것이 좋습니다.

- OSIV를 사용하는 경우의 장점:
    - 간편한 구현: OSIV를 사용하면, 영속성 컨텍스트가 자동으로 생성되고 유지되므로 구현이 간편합니다.
    - 지연로딩 사용 가능: OSIV를 사용하면, View Template에서 지연로딩 엔티티에 접근할 수 있습니다.
    - 세션 유지 가능: OSIV를 사용하면, HTTP 요청과 응답 사이에 영속성 컨텍스트를 유지하므로 세션을 유지할 수 있습니다.
- OSIV를 사용하는 경우의 단점:
    - 메모리 사용량 증가: OSIV를 사용하면, 영속성 컨텍스트가 계속해서 유지되므로 메모리 사용량이 증가할 수 있습니다.
    - 성능 저하: OSIV를 사용하면, 영속성 컨텍스트가 계속해서 유지되므로 성능이 저하될 수 있습니다.
    - 데이터 불일치 가능성: OSIV를 사용하면, 영속성 컨텍스트가 오랜 시간 동안 유지될 경우, 데이터 불일치 문제가 발생할 수 있습니다.
5. Summernote에서 사진만 넣으면 null을 반환한다.
   - png파일만 인식하기 때문에, jpg파일을 업로드하면 null을 반환한다.
