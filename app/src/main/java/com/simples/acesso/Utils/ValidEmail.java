package com.simples.acesso.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidEmail {
    public static boolean check (String email) {
        Matcher matcher = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email);
        return matcher.find();
    }
}
