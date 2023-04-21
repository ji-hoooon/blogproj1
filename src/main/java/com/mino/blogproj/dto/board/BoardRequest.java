package com.mino.blogproj.dto.board;

import com.mino.blogproj.model.board.Board;
import com.mino.blogproj.model.user.User;
import lombok.Getter;
import lombok.Setter;

public class BoardRequest {
    @Getter
    @Setter
    public static class SaveInDTO{
        //checkpoint: 유효성 검사 필요
        private String title;
        private String content;

        //checkpoint: 썸네일을 받아야 한다.
        public Board toEntity(User user){
            return Board.builder()
                    .user(user)
                    .title(title)
                    .content(content)
                    .thumbnail(null)
                    .build();
        }
    }
}
