package com.mino.blogproj.controller;

import com.mino.blogproj.core.auth.MyUserDetails;
import com.mino.blogproj.core.exception.ssr.Exception400;
import com.mino.blogproj.core.exception.ssr.Exception403;
import com.mino.blogproj.dto.user.UserRequest;
import com.mino.blogproj.model.user.User;
import com.mino.blogproj.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

//서비스에
@Transactional(readOnly = true)
@Controller
@RequiredArgsConstructor
public class UserController {

    private final HttpSession session;
    private final UserService userService;
    //URI 컨벤션
    // 인증이 되지 않은 상태에서 인증과 관련된 주소는 엔티티를 적지 않는다.
    // 행위: /리소스/식별자
    // POST 메서드로 DELETE, UPDATE, INSERT 모두 사용
    //write (post) : /리소스/식별자/(pk, uk) save or delete or update
    //read  (get)  : /리소스/식별자
    @PostMapping("/join")
    public String join(@Valid UserRequest.JoinInDTO joinInDTO, Errors errors){    //x-www-form-urlencoded
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

    //profileUpdateForm
    //프로필 업데이트하기:
    @GetMapping("/s/user/{id}/updateProfileForm")
    public String profileUpdateForm(@PathVariable Long id, Model model, @AuthenticationPrincipal MyUserDetails myUserDetails){
        // 1. 권한 체크
        if(id != myUserDetails.getUser().getId()){
            throw new Exception403("권한이 없습니다");
        }
        User userPS = userService.회원프로필보기(id);
        model.addAttribute("user", userPS);
        return "user/profileUpdateForm";
    }

    //프로필 업데이트하고 redirect
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

    //유저 상세보기
    //: 회원 수정 가능한 페이지 전달
    @GetMapping("/s/user/{id}/updateForm")
    public String updateForm(@PathVariable Long id, Model model, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        // 1. 권한 체크
        if(id != myUserDetails.getUser().getId()){
            throw new Exception403("권한이 없습니다");
        }
        // 2. 회원 정보 조회
        User userPS = userService.회원정보보기(id);
        model.addAttribute("user", userPS);
        return "user/updateForm";
    }
}
