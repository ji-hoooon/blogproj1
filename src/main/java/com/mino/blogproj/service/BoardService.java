package com.mino.blogproj.service;

import com.mino.blogproj.dto.board.BoardRequest;
import com.mino.blogproj.dto.board.BoardRequest.SaveInDTO;
import com.mino.blogproj.model.board.BoardRepository;
import com.mino.blogproj.model.user.User;
import com.mino.blogproj.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

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

}
