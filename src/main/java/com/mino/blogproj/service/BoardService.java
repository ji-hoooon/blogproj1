package com.mino.blogproj.service;

import com.mino.blogproj.core.exception.ssr.Exception400;
import com.mino.blogproj.core.util.MyParseUtil;
import com.mino.blogproj.dto.board.BoardQueryRepository;
import com.mino.blogproj.dto.board.BoardRequest;
import com.mino.blogproj.dto.board.BoardRequest.SaveInDTO;
import com.mino.blogproj.model.board.Board;
import com.mino.blogproj.model.board.BoardRepository;
import com.mino.blogproj.model.user.User;
import com.mino.blogproj.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private final BoardQueryRepository boardQueryRepository;
    //joinfetch한 리포지토리

    //insert, update,delete 시엔 Transactional
    @Transactional
    public void 글쓰기(BoardRequest.SaveInDTO saveInDTO, Long userId){
        //세션에 있는 아이디만 받는다. -> 실제 유저가 있는지 조회가 있어야 하므로
        //: 세션에 있는데 디비에 없을 수도 있다.

        //검증과 try-catch 로직 필수 (save에서 터질 수 있으므로)
        try{
            //1. 유저 존재 확인
            User userPS= userRepository.findById(userId).orElseThrow(
                    () -> new RuntimeException("해당 유저가 존재하지 않습니다.")
            );
            //2. 썸네일 만들기그

            String thumbnail= MyParseUtil.getThumbnail(saveInDTO.getContent());
            log.debug("디버그 : "+saveInDTO.getContent());
            System.out.println("디버그 : "+saveInDTO.getContent());

            //3. 게시글 쓰기
            boardRepository.save(saveInDTO.toEntity(userPS, thumbnail));

        }catch (Exception e){
            throw new RuntimeException("글쓰기 실패 : "+e.getMessage());
        }

    }

    @Transactional(readOnly = true)
    //변경감지 하지않기 위해, 고립성을 지키기 위해 (repeatable read)
//    public Page<Board> 글목록보기(Pageable pageable) {   //CSR은 DTO로 변경해서 돌려줘야 한다.
    public Page<Board> 글목록보기(int page) {   //CSR은 DTO로 변경해서 돌려줘야 한다.
//        return boardRepository.findAll(pageable);   //여기에선 아직 영속화된 상태 컨트롤러로 넘어가면 비영속상태
        //JPA가 Page로 바꿔서 준다.
//        return boardQueryRepository.findAll(pageable);   //여기에선 아직 영속화된 상태 컨트롤러로 넘어가면 비영속상태
        return boardQueryRepository.findAll(page);   //여기에선 아직 영속화된 상태 컨트롤러로 넘어가면 비영속상태
        //-> EntityGraph도 가능

        //Join fetch로 해결하는게 좋다.
        //: 컨트롤러에 User 객체가 없어서 터지는걸 방지하는 한가지 방법
//        Page<Board> boardsPGPS = boardRepository.findAll(pageable);
//        boardsPGPS.stream().forEach(board ->{
//            board.getUser().getUsername();
//        });
//        return boardsPGPS;
    }

    public Board 게시글상세보기(Long id) {
        //ManyToOne 관계이므로 조회2번보다 조인해서 1번 쿼리 발생시키는게 낫다.
        Board boardPS = boardRepository.findByIdFetchUser(id).orElseThrow(
                ()-> new Exception400("id", "게시글 아이디를 찾을 수 없습니다")
        );
        // 1. Lazy Loading 하는 것 보다 join fetch 하는 것이 좋다.
        // 2. 왜 Lazy를 쓰냐면, 쓸데 없는 조인 쿼리를 줄이기 위해서이다.
        // 3. 사실 @ManyToOne은 Eager 전략을 쓰는 것이 좋다.
        // boardPS.getUser().getUsername();
        return boardPS;
    }
//    public void subquery(){
//        // 엄청난 긴 쿼리를 짤때는, 결국 QueryDSL 사용하는게 좋음
//        String sql = "select id, title, content, (select count(id) from love) like_count, 1 n1,2 n2, 3 n3 from board where id = 1"; // 30줄
//        Query query = em.createNativeQuery(sql);
//        JpaResultMapper result = new JpaResultMapper();
//        MyDTO myDTO = result.uniqueResult(query, MyDTO.class);
//    }
//
//    public static class MyDTO {
//        private int id;
//        private String title;
//        private String content;
//        private int likeCount;
//        private int n1;
//        private int n2;
//        private int n3;
//    }
}

