package com.mino.blogproj.dto.board;

import com.mino.blogproj.model.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardQueryRepository {
    private final EntityManager em;
    private final int SIZE=8;


//    public Page<Board> findAll(Pageable pageable) {
    public Page<Board> findAll(int page) {
        int startPosition = page*SIZE;
        List<Board> boardListPS = em.createQuery("select b from Board b join fetch b.user order by b.id desc")  //최신글 순으로
                .setFirstResult(startPosition)   //  시작 번호
//                .setMaxResults(pageable.getPageSize())  //개수
                .setMaxResults(SIZE)  //개수
                .getResultList();
        Long totalCount = em.createQuery("select count(b) from Board b", Long.class).getSingleResult();
//        return new PageImpl<>(boardListPS, pageable, boardListPS.size());
        return new PageImpl<>(boardListPS, PageRequest.of(page, SIZE), totalCount);
        //사이즈 개수는 토탈 사이즈가 필요
    }
}