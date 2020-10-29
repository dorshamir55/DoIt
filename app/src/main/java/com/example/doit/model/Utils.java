package com.example.doit.model;

import android.util.Patterns;

import java.util.regex.Pattern;

public class Utils {
    public static boolean validateCredentials(String email, String password) {
        if(email == null || password == null)
            return false;

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return false;

        if(password.length() < 6)
            return false;

        return true;
    }

    public static boolean validateCredentials(String email, String password, String repeatPass) {
        return validateCredentials(email, password) && password.equals(repeatPass);
    }
}
