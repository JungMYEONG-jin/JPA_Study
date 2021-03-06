package com.instagram.controller;

import com.instagram.dto.signup.SignupDto;
import com.instagram.entity.User;
import com.instagram.service.AuthService;
import com.instagram.util.ValidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Slf4j
@RequiredArgsConstructor
@Controller
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/signin")
    public String signinForm(){
        return "auth/signin";
    }

    @GetMapping("/auth/signup")
    public String signupForm(@ModelAttribute SignupDto signupDto){
        return "auth/signup";
    }

    @PostMapping("/auth/signup")
    public String signup(@ModelAttribute SignupDto signupDto, BindingResult bindingResult){

        User user = signupDto.toEntity();

        if (!ValidUtils.isValidEmail(user.getEmail())){
            bindingResult.addError(new FieldError("signupDto", "email", user.getEmail(), false, null, null,"잘못된 이메일 형식입니다."));
        }

        if (bindingResult.hasErrors()){
            log.info("errors={}", bindingResult);
            return "auth/signup";
        }

        // 성공 로직
        authService.join(user);
        return "auth/signin";
    }

}
