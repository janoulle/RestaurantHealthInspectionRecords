package com.janeullah.apps.healthinspectionviewer;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;

import static com.google.common.io.Resources.getResource;

/**
 * https://www.stubbornjava.com/posts/reading-file-resources-with-guava
 */

public class TestingUtils {

    public static String asString(String resource) {
        URL url = getResource(resource);
        try {
            return Resources.toString(url, Charsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
