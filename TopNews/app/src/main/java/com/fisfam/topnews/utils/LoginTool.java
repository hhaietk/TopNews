package com.fisfam.topnews.utils;

import android.util.Patterns;

import java.util.regex.Pattern;

public class LoginTool {

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
        return pattern.matcher(password).matches();
    }
}
