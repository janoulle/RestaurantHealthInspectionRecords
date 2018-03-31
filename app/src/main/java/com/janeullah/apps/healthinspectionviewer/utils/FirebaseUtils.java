package com.janeullah.apps.healthinspectionviewer.utils;

import com.google.common.base.CharMatcher;

/**
 * @author Jane Ullah
 * @date 4/27/2017.
 */
public class FirebaseUtils {

    private FirebaseUtils() {}

    public static String replaceInvalidCharsInKey(String key) {
        return CharMatcher.anyOf("/.#$[]").replaceFrom(key, "");
    }
}
