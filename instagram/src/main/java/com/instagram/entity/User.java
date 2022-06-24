package com.instagram.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue
    @Column(name = "userID")
    private Long id;
    @Column
    private String bio;
    @Column
    private LocalDateTime createdDate;
    @Column(nullable = false)
    private String email;
    @Column
    private String gender;
    @Column(nullable = false)
    private String password; // 암호화해서 저장하자 sha256
    @Column(length = 20)
    private String phone;
    @Column
    private String role;
    @Column(length = 100)
    private String username;
    @Column
    private String website;

    @OneToMany(mappedBy = "user") // 유저는 여러 게시물을 올릴수 있음
    private List<Subscribe> subs = new ArrayList<>();

    @OneToMany(mappedBy = "user") // user can have many likes
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();



}
