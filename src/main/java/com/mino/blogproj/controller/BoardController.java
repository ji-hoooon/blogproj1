package com.mino.blogproj.controller;

import com.mino.blogproj.core.auth.MyUserDetails;
import com.mino.blogproj.dto.board.BoardRequest;
import com.mino.blogproj.model.board.Board;
import com.mino.blogproj.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;
    private final Logger log = LoggerFactory.getLogger(getClass());
    //RestAPI 주소 설계 규칙에서 자원은 복수를 붙인다. -> noSQL

    //: 협업 컨벤션
//    @GetMapping({"/", "/board"})
//    //@RequestParam은 기본값 설정이 필요할 때 사용한다.
//    public @ResponseBody Page<Board> main(@RequestParam(defaultValue = "0") Integer page, Model model){   //쿼리스트링 Pathvariable X, Null처리하기위해 래핑클래스 사용
////    public String main(@RequestParam(defaultValue = "0") Integer page){   //쿼리스트링 Pathvariable X, Null처리하기위해 래핑클래스 사용
////    public String main(@AuthenticationPrincipal MyUserDetails myUserDetails){
////        log.debug("디버그 : "+myUserDetails.getUser().getUsername());
//
//        PageRequest pageRequest=PageRequest.of(page, 8, Sort.by("id").descending());
//        //@PageDefault, Pageable과 차이가 뭘까 -어노테이션은 비추
////        Page<Board> boardPG = boardService.글목록보기(pageRequest);
//        Page<Board> boardPG = boardService.글목록보기(page);
//        //OSIV가 꺼져있어서 비영속이라고 봐야한다 (영속성컨텍스트엔 존재하지만, 조회가 불가능한 세션에 연결이 안된다. 즉, 변경감지 불가능
//        //OSIV가 켜져있으면 영속 상태
//
//        //return "board/main";
//
//        //뷰에서 사용하기 위해서 모델에 추가한다.
//        model.addAttribute("boardPG", boardPG);
//
//        return boardPG;
//    }
    // RestAPI 주소 설계 규칙에서 자원에는 복수를 붙인다. boards 정석!!
    @GetMapping({"/", "/board"})    //where에 PK , UK가 아니므로 QueryString 사용
    public String main(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String keyword,
            Model model
    ){
        //전체를 줄지, 부분만을 줄지만 달라지는 컨트롤러의 역할
        Page<Board> boardPG = boardService.글목록보기(page, keyword);
        model.addAttribute("boardPG", boardPG);
//        model.addAttribute("keyword", keyword);

        return "board/main";
    }

    @GetMapping("/s/board/saveForm")
    public String saveForm(){
        return "board/saveForm";
    }
    @PostMapping("/s/board/save")
    public String save(BoardRequest.SaveInDTO saveInDTO, @AuthenticationPrincipal MyUserDetails myUserDetails){
        log.debug("디버그 : "+myUserDetails.getUser().getId());
        boardService.글쓰기(saveInDTO, myUserDetails.getUser().getId());
        return "redirect:/";
    }

    //게시글 상세보기
    @GetMapping( "/board/{id}")
    public String detail(@PathVariable Long id, Model model){
        Board board = boardService.게시글상세보기(id);
        model.addAttribute("board", board);
        return "board/detail";
    }
    //모델에 담아야지 EL 표현식으로 꺼낼 수 있다.
    //: reqeust에도 담아도 된다. -> RequestDispatcher -> request 덮어쓰기 기술

}
