package com.simpleauthJPA.shinhan.security.callback;

public interface PasswordListener {

    public boolean CheckPasswordValidation(String password, String authType);
}

