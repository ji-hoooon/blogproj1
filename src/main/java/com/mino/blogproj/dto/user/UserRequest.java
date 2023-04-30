package com.mino.blogproj.dto.user;

import com.mino.blogproj.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class UserRequest {

    @Getter
    @Setter
    public static class JoinInDTO{
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
        @NotEmpty
        private String email;

        //DTO -> Entity 변환을 위한 메서드 작성
        public User toEntity(){
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role("USER")   //checkpoint: enum 사용하도록 변경
                    .status(true)
                    .profile("person.png")  //checkpoint: 프로필 사진추가
                    .build();
        }
    }
}
