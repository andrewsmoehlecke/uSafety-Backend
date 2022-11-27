package com.api.usafety_backend.util;

import java.util.regex.Pattern;

public class RegexValidador {

    public static boolean validador(String conteudo, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(conteudo)
                .matches();
    }
}