package com.mino.blogproj;

import com.mino.blogproj.model.board.Board;
import com.mino.blogproj.model.board.BoardRepository;
import com.mino.blogproj.model.user.User;
import com.mino.blogproj.model.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class BlogprojApplication extends DummyEntity{


    //개발중에서만 사용하는 어노테이션
    @Profile("dev")
    //화면 테스트시 회원가입하는 과정을 하지 않기 위해서
    @Bean
    CommandLineRunner init(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, BoardRepository boardRepository) {
        return args -> {
//            User ssar = User.builder()
//                    .username("ssar")
//                    .password(passwordEncoder.encode("1234"))
//                    .email("ssar@nate.com")
//                    .role("USER")
//                    .profile("person.png")
//                    .build();
//            User cos = User.builder()
//                    .username("cos")
//                    .password(passwordEncoder.encode("1234"))
//                    .email("cos@nate.com")
//                    .role("USER")
//                    .profile("person.png")
//                    .build();
            User ssar = newUser("ssar", passwordEncoder);
            User cos = newUser("cos", passwordEncoder);
            List<Board> boardList = new ArrayList<>();
            for (int i = 1; i < 11; i++) {
                boardList.add(newBoard("제목"+i, ssar));
            }
            for (int i = 11; i < 21; i++) {
                boardList.add(newBoard("제목"+i, ssar));
            }
            userRepository.saveAll(Arrays.asList(ssar, cos));

//            Board b1 = Board.builder()
//                    .title("제목1")
//                    .content("내용1")
//                    .user(ssar)
//                    .thumbnail("/upload/person.png")
//                    .build();
//            Board b2 = Board.builder()
//                    .title("제목2")
//                    .content("내용2")
//                    .user(cos)
//                    .thumbnail("/upload/person.png")
//                    .build();
//            boardRepository.saveAll(Arrays.asList(b1, b2));


            boardRepository.saveAll(boardList);

        };
    }
    public static void main(String[] args) {
        SpringApplication.run(BlogprojApplication.class, args);
    }

}
