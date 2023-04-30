package com.mino.blogproj.controller.api;

import com.mino.blogproj.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//JSON을 반환하는 API 컨트롤러
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserApiController {

    private final UserService userService;

    // 식별자에는 PK, UK
    // 자원/식별자/물음동사 (GET)
    // 자원/식별자/변경동사 (POST)
    @GetMapping("/user/{username}/sameUsername")
    public ResponseEntity<?> sameUsername(@PathVariable String username){
        userService.유저네임중복체크(username);
        return ResponseEntity.ok().build();
    }
}