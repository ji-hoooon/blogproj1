package com.mino.blogproj.model.board;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mino.blogproj.DummyEntity;
import com.mino.blogproj.model.board.Board;
import com.mino.blogproj.model.user.User;
import com.mino.blogproj.model.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ActiveProfiles("test")
@Import(BCryptPasswordEncoder.class)
@DataJpaTest
public class BoardRepositoryTest extends DummyEntity {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    private void join_good() {
        //10건 중 모두 다른 사람일 떄
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        List<User> userList = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            userList.add(newUser("user" + i, passwordEncoder));
        }
        userRepository.saveAll(userList);

        List<Board> boardList = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            boardList.add(newBoard("제목" + i, userList.get(i - 1)));
        }
        boardRepository.saveAll(boardList);
        em.clear();
    }

    private void in_query_good() {
        //10건 중 3, 3, 4 비율로 같은 사람일 떄
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        List<User> userList = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            userList.add(newUser("user" + i, passwordEncoder));
        }
        userRepository.saveAll(userList);

        List<Board> boardList = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            boardList.add(newBoard("제목" + i, userList.get(0)));
        }
        for (int i = 5; i < 9; i++) {
            boardList.add(newBoard("제목" + i, userList.get(1)));
        }
        for (int i = 9; i < 11; i++) {
            boardList.add(newBoard("제목" + i, userList.get(2)));
        }
        boardRepository.saveAll(boardList);
        em.clear();
    }

    @BeforeEach
    public void setUp() {
        join_good();
    }

    @Test
    public void findAll_test() {
        // Lazy 전략 -> Board만 조회
        boardRepository.findAll();
    }

    @Test
    public void findAllV2_test() {
        // Lazy 전략 -> Board만 조회
        List<Board> boardList = boardRepository.findAll();
        // Lazy Loading 하기 (20바퀴)
        boardList.stream().forEach(board -> {
            System.out.println(board.getUser().getUsername()); // select 2번
        });
    }

    @Test
    public void findAllV3_test() throws JsonProcessingException {
        // Lazy 전략 -> Board만 조회
        //: page 객체를 사용하면 findAll() 사용시 N+1 발생
        PageRequest pageRequest = PageRequest.of(0, 8, Sort.by("id").descending());
        Page<Board> boardPG = boardRepository.findAll(pageRequest);

        String responseBody = new ObjectMapper().writeValueAsString(boardPG);
        //USER가 프록시 객체이므로 터진다.
        //: 테스트를 위해 날짜타입에 @JsonIgnore를 붙인다.
        System.out.println(responseBody);
    }

    @Test
    public void findByUserId_test() {
        List<Board> boardList = boardRepository.findByUserId(1L);
        boardList.stream().forEach(board -> {
            // loop 1번에 select  (ssar)
            // loop 2~10번 1차 캐싱 (PC)
            // loop 11번에 select  (cos)
            // loop 11~20번 1차 캐싱 (PC)
            System.out.println(board.getUser().getUsername()); // select 2번
        });
    }


    @Test
    public void findAllFetchUser_test() throws Exception {
        //given
        boardRepository.findAllWithFetchUser();

        //when

        //then

    }

    @Test
    public void findByUserIds_test() throws Exception {
        //given
        List<Long> userIds = boardRepository.findUserIdDistinct();
        List<Board> boardList = boardRepository.findByUserIds(userIds);
        System.out.println("===================================");
        System.out.println(boardList.get(0).getUser().getUsername());
    }
    @Test
    public void findByUserIds2_test() throws Exception {
        //given
        List<Long> userIds = boardRepository.findUserIdDistinct();
        //찾은 유저를 모두 영속화시킨다.
        List<User> userList=userRepository.findByUserIds(userIds);

        System.out.println(userList.get(0).getUsername());
    }

}