package com.mino.blogproj.service;

import com.mino.blogproj.core.exception.csr.ExceptionApi400;
import com.mino.blogproj.core.exception.ssr.Exception400;
import com.mino.blogproj.core.exception.ssr.Exception500;
import com.mino.blogproj.core.util.MyFileUtil;
import com.mino.blogproj.dto.user.UserRequest;
import com.mino.blogproj.model.user.User;
import com.mino.blogproj.model.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${file.path}")
    private String uploadFolder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;  //SecurityConfig에 빈으로 등록했기 때문에

    @Transactional
    //insert, update, delete -> 직접 try catch 처리

    //insert, update 같은 경우 트랜잭션 고립성 발생 -> 느려짐
    //: insert, update는 완벽할 때만 안전하게 수행하도록 한다. (중복체크, 용량 압축) -> I/O를 줄인다.
    public void 회원가입(UserRequest.JoinInDTO joinInDTO){
        //1. 유저네임 중복확인
        Optional<User> userOP = userRepository.findByUsername(joinInDTO.getUsername());
        if(userOP.isPresent()){
            // 비정상적인 접근
            log.error("디버그 : 회원가입시  비정상적인 접근으로 유저네임 중복");
            throw new Exception400("username", "유저네임이 중복되었어요");
        }
        //:try-catch문 밖에 둬야 한다.
        try {
            //2. 패스워드 암호화 필요
            joinInDTO.setPassword(bCryptPasswordEncoder.encode(joinInDTO.getPassword()));

            //3. DB 저장
            userRepository.save(joinInDTO.toEntity());
        }catch (Exception e){
            throw new Exception500("회원가입 실패"+e.getMessage());
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

    //UserApiController에서 호출하는 메서드
    public void 유저네임중복체크(String username) {
        Optional<User> userOP = userRepository.findByUsername(username);
        if(userOP.isPresent()){
            throw new ExceptionApi400("username", "유저네임이 중복되었어요");
        }
    }

    //회원 상세보기 -> 회원수정
    public User 회원정보보기(Long id) {
        User userPS = userRepository.findById(id)
                .orElseThrow(()->new Exception400("id", "해당 유저가 존재하지 않습니다"));
        return userPS;
    }
}