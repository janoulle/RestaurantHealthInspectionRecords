package com.janeullah.apps.healthinspectionviewer.utils;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.crash.FirebaseCrash;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * http://pokusak.blogspot.com/2015/10/aws-elasticsearch-request-signing.html
 * https://github.com/aws/aws-sdk-java/blob/master/aws-java-sdk-core/src/main/java/com/amazonaws/auth/AWS4Signer.java
 * https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
 * @date 9/26/2017.
 */

public class AwsUtilities {
    private static final String TAG = "AwsUtilities";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();


    private AwsUtilities(){}

    public static byte[] hmacSHA256(String data, byte[] key) throws Exception {
        String algorithm="hmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data.getBytes("UTF8"));
    }

    public static byte[] getSignatureKey(String secretKey, String dateStamp, String regionName, String serviceName) throws Exception {
        byte[] kSecret = ("AWS4" + secretKey).getBytes("UTF8");
        byte[] kDate = hmacSHA256(dateStamp, kSecret);
        byte[] kRegion = hmacSHA256(regionName, kDate);
        byte[] kService = hmacSHA256(serviceName, kRegion);
        return hmacSHA256("aws4_request", kService);
    }

    public static String convertByteArrayToHexadecimalAsLowercase(byte[] bytes) {
        try {
            char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; j++) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            return new String(hexChars).toLowerCase();
        }catch(Exception e){
            FirebaseCrash.report(e);
            Log.d(TAG,"failed to convert bytes"  + bytes +  " to hex",e);
        }
        return "";
    }

    /**
     * http://www.baeldung.com/sha-256-hashing-java
     * https://stackoverflow.com/questions/5531455/how-to-hash-some-string-with-sha256-in-java
     * @param data String to be hashed
     * @return byte representation of the hash
     */
    public static byte[] hashPayload(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(data.getBytes("UTF-8"));
        }catch(Exception e){
            FirebaseCrash.report(e);
            Log.e(TAG,"Failed to hash data=" + data);
        }
        return new byte[0];
    }

    public static <T> String getString(T request){
        try{
            return mapper.writeValueAsString(request);
        }catch(Exception e){
            FirebaseCrash.report(e);
            Log.e(TAG, "Failed to convert request (" + request + ") to string");
        }
        return "";
    }

    public static <T> InputStream generateInputStream(T request){
        try{
            return new ByteArrayInputStream(mapper.writeValueAsBytes(request));
        }catch(Exception e){
            FirebaseCrash.report(e);
            Log.e(TAG,"Failure to convert request=" + request + " to input stream",e);
        }
        return null;
    }
}
