package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import hello.jdbc.repository.MemberRepositoryV3;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberServiceV3_1Test {

    public static final String MA = "memberA";
    public static final String MB = "memberB";
    public static final String EX = "ex";

    private MemberRepositoryV3 memberRepository;
    private MemberServiceV3_1 memberService;

    @BeforeEach
    void init(){
        DriverManagerDataSource data = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepository = new MemberRepositoryV3(data);
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(data);
        memberService = new MemberServiceV3_1(transactionManager,memberRepository);
    }

    @AfterEach
    void after() throws SQLException {
        memberRepository.delete(MA);
        memberRepository.delete(MB);
        memberRepository.delete(EX);
    }


    @Test
    @DisplayName("normal")
    void transferTest() throws SQLException {
        Member memberA = new Member(MA, 10000);
        Member memberB = new Member(MB, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        Member findA = memberRepository.findById(MA);
        Member findB = memberRepository.findById(MB);

        assertThat(findA.getMoney()).isEqualTo(8000);
        assertThat(findB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("UnNormal")
    void transferTestFail() throws SQLException {
        Member memberA = new Member(MA, 10000);
        Member memberEX = new Member(EX, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberEX);

        assertThatThrownBy(()->memberService.accountTransfer(memberA.getMemberId(), memberEX.getMemberId(), 2000)).isInstanceOf(IllegalStateException.class);


        Member findA = memberRepository.findById(MA);
        Member findEX = memberRepository.findById(EX);

        assertThat(findA.getMoney()).isEqualTo(10000);
        assertThat(findEX.getMoney()).isEqualTo(10000);
    }




}