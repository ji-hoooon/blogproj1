package com.mino.blogproj.service;

import com.mino.blogproj.dto.board.BoardQueryRepository;
import com.mino.blogproj.dto.board.BoardRequest;
import com.mino.blogproj.dto.board.BoardRequest.SaveInDTO;
import com.mino.blogproj.model.board.Board;
import com.mino.blogproj.model.board.BoardRepository;
import com.mino.blogproj.model.user.User;
import com.mino.blogproj.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {
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

            //2. 게시글 쓰기
            boardRepository.save(saveInDTO.toEntity(userPS));

        }catch (Exception e){
            throw new RuntimeException("글쓰기 실패 : "+e.getMessage());
        }

    }

    @Transactional(readOnly = true)
    //변경감지 하지않기 위해, 고립성을 지키기 위해 (repeatable read)
    public Page<Board> 글목록보기(Pageable pageable) {   //CSR은 DTO로 변경해서 돌려줘야 한다.
//        return boardRepository.findAll(pageable);   //여기에선 아직 영속화된 상태 컨트롤러로 넘어가면 비영속상태
        //JPA가 Page로 바꿔서 준다.
        return boardQueryRepository.findAll(pageable);   //여기에선 아직 영속화된 상태 컨트롤러로 넘어가면 비영속상태
        //-> EntityGraph도 가능

        //Join fetch로 해결하는게 좋다.
        //: 컨트롤러에 User 객체가 없어서 터지는걸 방지하는 한가지 방법
//        Page<Board> boardsPGPS = boardRepository.findAll(pageable);
//        boardsPGPS.stream().forEach(board ->{
//            board.getUser().getUsername();
//        });
//        return boardsPGPS;
    }
}
