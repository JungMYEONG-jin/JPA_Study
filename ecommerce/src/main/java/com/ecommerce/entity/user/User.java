package com.ecommerce.entity.user;

import com.ecommerce.base.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static org.hibernate.annotations.CascadeType.ALL;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @OneToMany(mappedBy = "user")
    @Cascade(ALL)
    private Set<Authority> authorities = new HashSet<>();

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    private String phoneNumber;
    private boolean rocketMembership;
}
