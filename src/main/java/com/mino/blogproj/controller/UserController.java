package com.mino.blogproj.controller;

import com.mino.blogproj.dto.UserRequest;
import com.mino.blogproj.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    //URI 컨벤션
    // 인증이 되지 않은 상태에서 인증과 관련된 주소는 엔티티를 적지 않는다.
    // 행위: /리소스/식별자
    // POST 메서드로 DELETE, UPDATE, INSERT 모두 사용
    //write (post) : /리소스/식별자/(pk, uk) save or delete or update
    //read  (get)  : /리소스/식별자
    @PostMapping("/join")
    public String join(UserRequest.JoinInDTO joinInDTO){    //x-www-form-urlencoded
        //모든 로직을 서비스에게 위임한다.

        userService.회원가입(joinInDTO);
        return "redirect:/loginForm";   //302
    }//회원가입시 로그인폼 혹은 요청했던 페이지로 이동

    //JoinForm
    @GetMapping("/joinForm")
    public String joinForm(){
        return "user/joinForm";
    }

    //LoginForm
    @GetMapping("/loginForm")
    public String loginForm(){
        return "user/loginForm";
    }
    //모든 뷰를 반환은 ViewResolver로 간다.
}
