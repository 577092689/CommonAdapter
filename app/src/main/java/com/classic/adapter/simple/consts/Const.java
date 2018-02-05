package com.classic.adapter.simple.consts;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Const {
    private Const(){}

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    public static final String URL_SEPARATOR = ";";
    public static final String FORMAT_AUTHOR = "报道人：%s";
}
