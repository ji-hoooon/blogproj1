package com.mino.blogproj.service;

import com.mino.blogproj.core.exception.ssr.Exception400;
import com.mino.blogproj.core.exception.ssr.Exception500;
import com.mino.blogproj.core.util.MyFileUtil;
import com.mino.blogproj.dto.user.UserRequest;
import com.mino.blogproj.model.user.User;
import com.mino.blogproj.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class UserService {

    @Value("${file.path}")
    private String uploadFolder;
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

    public User 회원프로필보기(Long id) {
        User userPS = userRepository.findById(id)
                .orElseThrow(()->new Exception400("id", "해당 유저가 존재하지 않습니다"));
        return userPS;
    }

    @Transactional
    public User 프로필사진수정(MultipartFile profile, Long id) {
        try {
            //uploadFolder는 프로퍼티로 설정
            String uuidImageName = MyFileUtil.write(uploadFolder, profile);
            User userPS = userRepository.findById(id)
                    .orElseThrow(()->new Exception500("로그인 된 유저가 DB에 존재하지 않음"));
            userPS.changeProfile(uuidImageName);
            return userPS;
        }catch (Exception e){
            throw new Exception500("프로필 사진 등록 실패 : "+e.getMessage());
        }
    }//트랜잭션 종료로 인한 더티체킹 발생 -> 변경감지
}