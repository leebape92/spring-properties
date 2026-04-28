package com.example.demo.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class JasyptUtil {
    public static StandardPBEStringEncryptor getEncryptor(String password) {
        StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
        jasypt.setPassword(password);
        jasypt.setAlgorithm("PBEWithMD5AndDES");
        return jasypt;
    }
}
