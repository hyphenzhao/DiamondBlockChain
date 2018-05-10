/***************************************************************************************
*    Code Citation
*    Title: phishman3579/Bitcoin
*    Author: Justin Wetherell
*    Date: 2017
*    Code version: 4ed6be50a2a63a2a51c988daf3ad717e545a1b3f
*    Availability: https://github.com/phishman3579/Bitcoin
*
***************************************************************************************/
package com.uts.DBC.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public abstract class HashUtils {

    public static final byte[] calculateSha256(String text) {
        byte[] hash2;
        try {
            hash2 = calculateSha256(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return hash2;
    }

    public static final byte[] calculateSha256(byte[] utf8Bytes) {
        byte[] hash2;
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash1 = digest.digest(utf8Bytes);
            hash2 = digest.digest(hash1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return hash2;
    }

    public static final String bytesToHex(byte[] bytes) {
        final StringBuilder result = new StringBuilder();
        for (byte byt : bytes) 
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
    
    /***************************************************************************************
    *    Code Citation
    *    Title: pConvert a string representation of a hex dump to a byte array using Java?
    *    Author: Dave L.
    *    Date: 2008
    *    Code version: None
    *    Availability: https://stackoverflow.com/questions/140131/convert-a-string-representation-of-a-hex-dump-to-a-byte-array-using-java
    *
    ***************************************************************************************/
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

}
