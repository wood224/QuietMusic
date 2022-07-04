package com.example.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsPhone {
    public static boolean checkNum(String phone){
        if (phone == null || phone.trim().equals("")) {
            return false;
        }
        if (phone.length() != 11) {
            return false;
        }
        Pattern pattern = Pattern.compile("^1[3-9]\\d{9}$");
        Matcher matcher = pattern.matcher(phone);

        return matcher.matches();

    }
}
