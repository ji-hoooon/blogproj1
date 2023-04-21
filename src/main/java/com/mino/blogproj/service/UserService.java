package com.mino.blogproj.service;

import com.mino.blogproj.dto.user.UserRequest;
import com.mino.blogproj.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;  //SecurityConfig에 빈으로 등록했기 때문에

    @Transactional
    //insert, update, delete -> 직접 try catch 처리
    public void 회원가입(UserRequest.JoinInDTO joinInDTO){
        try {
            //1. 패스워드 암호화 필요
            joinInDTO.setPassword(bCryptPasswordEncoder.encode(joinInDTO.getPassword()));
            //2. DB 저장
            userRepository.save(joinInDTO.toEntity());
        }catch (Exception e){
            throw new RuntimeException("회원가입 오류"+e.getMessage());
        }//: 컨트롤러로 처리하지 않고, save 내부에서 터진 서비스의 책임으로 직접 처리해야한다. -> throw로 던지지 못한다.

    } //트랜잭션 종료시 : 더티 체킹, DB 세션 종료(OSIV=False이기 때문에)
}
