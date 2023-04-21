package com.mino.blogproj.controller;

import com.mino.blogproj.core.auth.MyUserDetails;
import com.mino.blogproj.dto.board.BoardRequest;
import com.mino.blogproj.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;
    private final Logger log = LoggerFactory.getLogger(getClass());
    //RestAPI 주소 설계 규칙에서 자원은 복수를 붙인다. -> noSQL

    //: 협업 컨벤션
    @GetMapping({"/", "/board"})
    public String main(){
//    public String main(@AuthenticationPrincipal MyUserDetails myUserDetails){
//        log.debug("디버그 : "+myUserDetails.getUser().getUsername());
        return "board/main";
    }

    @GetMapping("/s/board/saveForm")
    public String saveForm(){

        return "board/saveForm";
    }
    @GetMapping("/s/board/save")
    public String save(BoardRequest.SaveInDTO saveInDTO, @AuthenticationPrincipal MyUserDetails myUserDetails){
        boardService.글쓰기(saveInDTO, myUserDetails.getUser().getId());
        return "redirect:/";
    }
}
