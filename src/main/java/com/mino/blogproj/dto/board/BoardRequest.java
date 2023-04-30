package com.mino.blogproj.dto.board;

import com.mino.blogproj.model.board.Board;
import com.mino.blogproj.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class BoardRequest {
    @Getter
    @Setter
    public static class SaveInDTO{
        //checkpoint: 유효성 검사 필요
        @NotEmpty
        private String title;
        @NotEmpty
        private String content;

        //checkpoint: 썸네일을 받아야 한다.
        public Board toEntity(User user, String thumbnail){
            return Board.builder()
                    .user(user)
                    .title(title)
                    .content(content)
                    .thumbnail(thumbnail)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class UpdateInDTO{
        //checkpoint: 유효성 검사 필요
        @NotEmpty
        private String title;
        @NotEmpty
        private String content;

        //checkpoint: 썸네일을 받아야 한다.
        public Board toEntity(User user, String thumbnail){
            return Board.builder()
                    .user(user)
                    .title(title)
                    .content(content)
                    .thumbnail(thumbnail)
                    .build();
        }
    }
}
