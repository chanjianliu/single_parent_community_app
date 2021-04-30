package com.team9.spda_team9.Authentication;

import android.util.Patterns;

public class ValidationUtil {
    public static Boolean emailValidation(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static Boolean passwordValidation(String password) {
        return password.trim().length() >= 6
                && password.trim().matches(".*[a-z].*+")
                && password.trim().matches(".*[A-Z].*+")
                && password.trim().matches(".*[0-9].*+")
                && password.trim().matches(".*[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？].*");
    }

    public static Boolean userNameValidation(String userName) {
        return userName.trim().length() > 0;
    }

    public static Boolean fullNameValidation(String fullName) {
        return fullName.trim().length() > 0;
    }

    public static Boolean notEmptyValidation(String target) {
        return target.trim().length() > 0;
    }

    public static Boolean locationValidation(String target) {
        return target.trim().length() == 6;
    }

    public static Boolean childrenValidation(String target) {
        return target.trim().length() < 2;
    }
}
