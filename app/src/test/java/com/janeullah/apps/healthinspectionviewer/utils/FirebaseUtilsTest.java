package com.janeullah.apps.healthinspectionviewer.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Jane Ullah
 * @date 10/22/2017.
 */

public class FirebaseUtilsTest {

    @Test
    public void testSlashReplacement_Success(){
        String stringWithSlash = "Rico\'s Place";
        String cleanedString = FirebaseUtils.replaceInvalidCharsInKey(stringWithSlash);
        assertEquals("Rico's Place",cleanedString);
    }

    @Test
    public void testDotReplacement_Success(){
        String stringWithSlash = "Rico.'s Place";
        String cleanedString = FirebaseUtils.replaceInvalidCharsInKey(stringWithSlash);
        assertEquals("Rico's Place",cleanedString);
    }

    @Test
    public void testOcthorpeReplacement_Success(){
        String stringWithSlash = "Rico#\'s Place";
        String cleanedString = FirebaseUtils.replaceInvalidCharsInKey(stringWithSlash);
        assertEquals("Rico's Place",cleanedString);
    }

    @Test
    public void testBracketReplacement_Success(){
        String stringWithSlash = "Rico's [Place] #272";
        String cleanedString = FirebaseUtils.replaceInvalidCharsInKey(stringWithSlash);
        assertEquals("Rico's Place 272",cleanedString);
    }

    @Test
    public void testBadCharReplacement_Success(){
        String stringWithSlash = "/.#$[]";
        String cleanedString = FirebaseUtils.replaceInvalidCharsInKey(stringWithSlash);
        assertEquals("",cleanedString);
    }

    @Test
    public void testCleanStringNoReplacement(){
        String testString = "Rico's Diner";
        String cleanedString = FirebaseUtils.replaceInvalidCharsInKey(testString);
        assertEquals(testString,cleanedString);
    }
}
