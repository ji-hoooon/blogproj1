package com.mino.blogproj;

import com.mino.blogproj.model.user.User;
import com.mino.blogproj.model.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BlogprojApplication {


    //개발중에서만 사용하는 어노테이션
    @Profile("dev")
    //화면 테스트시 회원가입하는 과정을 하지 않기 위해서
    @Bean
    CommandLineRunner init(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        return args -> {
            User ssar = User.builder()
                    .username("ssar")
                    .password(passwordEncoder.encode("1234"))
                    .email("ssar@nate.com")
                    .role("USER")
                    .profile("person.png")
                    .status(true)
                    .build();
            userRepository.save(ssar);
        };
    }
    public static void main(String[] args) {
        SpringApplication.run(BlogprojApplication.class, args);
    }

}
