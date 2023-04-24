package com.mino.blogproj.model.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select b from Board b where b.user.id=:userId")
    List<Board> findByUserId(@Param("userId") Long userId);

    //글쓴이가 다 다르면 조인 페치 수행
    @Query("select b from Board b join fetch b.user")
    List<Board> findAllWithFetchUser();

    //글쓴이 비율이 334일 때 inQuery 수행
    @Query("select distinct b.user.id from Board b")
    List<Long> findUserIdDistinct();

    //인쿼리 수행하면 발생하는 쿼리
    @Query("select b from Board b where b.user.id in :userIds")
    List<Board> findByUserIds(@Param("userIds") List<Long> userIds);

    //ManyToOne 관계이므로 조회2번보다 조인해서 1번 쿼리 발생시키는게 낫다.
    @Query("select b from Board b join fetch b.user where b.id = :id")
    Optional<Board> findByIdFetchUser(@Param("id") Long id);

}
