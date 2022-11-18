package com.sfp.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Tool {
    public MD5Tool() {
    }

    public static String md5(byte[] data) {
        MessageDigest md5 = null;

        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var5) {
            var5.printStackTrace();
            throw new IllegalStateException("System doesn't support MD5 algorithm.");
        }

        md5.update(data);
        byte[] encoded = md5.digest();
        StringBuffer buf = new StringBuffer();

        for(int i = 0; i < encoded.length; ++i) {
            if ((encoded[i] & 255) < 16) {
                buf.append("0");
            }
            buf.append(Long.toString((long)(encoded[i] & 255), 16));
        }
        return buf.toString();
    }

    public static String md5(String str, String charset) {
        if (str == null) {
            return null;
        } else {
            try {
                return md5(str.getBytes(charset));
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
                throw new IllegalStateException("System doesn't support Charset '" + charset + "'");
            }
        }
    }
}
