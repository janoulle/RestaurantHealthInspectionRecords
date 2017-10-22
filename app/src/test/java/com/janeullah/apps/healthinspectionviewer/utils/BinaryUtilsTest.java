package com.janeullah.apps.healthinspectionviewer.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Jane Ullah
 * @date 10/22/2017.
 */

public class BinaryUtilsTest {
    String testString = "pick up 5 oranges";
    String expectedHexConversion = "7069636b2075702035206f72616e676573";

    @Test
    public void testStringToHexConversion_Success() throws Exception{
        String actualHexConversion = BinaryUtils.toHex(testString.getBytes("UTF-8"));
        assertEquals(expectedHexConversion,actualHexConversion);
    }

    @Test
    public void testHexToStringConversion_Success() throws Exception{
        String actualStringConversion = new String(BinaryUtils.fromHex(expectedHexConversion));
        assertEquals(testString,actualStringConversion);
    }
}
