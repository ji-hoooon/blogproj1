package com.mino.blogproj.model.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //  직접 변경 불가능하도록
@Getter
@Table(name = "user_tb")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 20)
    private String username;
    @Column(length = 60)    //실제 길이는 20자 이유
    private String password;
    @Column(length = 50)
    private String email;
    private String role; // USER(고객)
    private String profile; //유저 프로필 사진의 경로
    private Boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    //의미 있는 메서드 -> setter만 작성
    // 프로필 사진 변경
    public void changeProfile(String profile){
        this.profile = profile;
    }

    //회원 정보 수정
    public void update(String password, String email) {
        this.password = password;
        this.email = email;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
