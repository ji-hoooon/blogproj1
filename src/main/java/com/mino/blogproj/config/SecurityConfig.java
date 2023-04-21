package com.mino.blogproj.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.FilterChain;

//@Slf4j
@Configuration
public class SecurityConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Bean
    //싱글톤으로 IoC에 등록해서 사용
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    //커스터마이징
    //1. CSRF 해제
    //2. awareFilter
    //
    SecurityFilterChain FilterChain(HttpSecurity http) throws  Exception{
        //1. CSRF 해제 - 운영시에는 프론트엔드 주소만 열기
        http.csrf().disable();
        //1-2. iframe 해제 (시큐리티 h2-console 접속 허용을 위해)
        http.headers().frameOptions().disable();
        //2. form 로그인 설정
        //: 커멜 표기법 -> 코드컨벤션
        //-> 하이픈이 원래 표준이지만, JSP와 구분하기 위해
        http.formLogin()
                //(1) 로그인페이지 설정
                .loginPage("/loginForm")
                //(2) 로그인 프로세스 설정
                .loginProcessingUrl("/login")
                //(3) 기본적인 핸들러 설정 성공시엔 기본 페이지 실패시엔 로그인폼
                .successHandler((request, response, authentication) ->{
                    log.debug("디버그 : 로그인 성공");
                    response.sendRedirect("/");

                })
                //(3) 엔트리 포인트 설정 -> 인증과 권한 실패시 -> TranslationFilter가 호출
                .failureHandler((request, response, exception) ->{
                    log.debug("디버그 : 로그인 실패"+exception.getMessage());
                    response.sendRedirect("/loginForm");

                });
        //3. 인증, 권한 필터 설정
//        http.authorizeRequests(expressionInterceptUrlRegistry -> )
        http.authorizeRequests(authorize ->
            //주소에 대한 권한, 인증 설정
           authorize.antMatchers("/s/**").authenticated()
                   //로그인 필요한 주소 (URI)
                   .anyRequest().permitAll()
        );
        return http.build();
    }

}
