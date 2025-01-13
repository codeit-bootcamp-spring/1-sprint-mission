package com.sprint.mission.discodeit.entity.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Encrypt {
    public static String getEncryptedPassword(String password) {
        String result = "";
        String salt = getSalt();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            System.out.println("암호화 적용 전: " + password);
            md.update((password + salt).getBytes());
            byte[] bytes = md.digest();

            StringBuffer sb = new StringBuffer();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            result = sb.toString();
            System.out.println("암호화 적용 후 " + result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[20];
        random.nextBytes(salt);
        StringBuilder sb = new StringBuilder();
        for (byte s : salt) {
            sb.append(String.format("%02x", s));
        }
        return sb.toString();
    }
}
