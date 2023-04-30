package com.mino.blogproj.model.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.username= :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("select u from User u where u.username in :username")
    List<User> findByUserIds(@Param("username") List<Long> userIds);

}
