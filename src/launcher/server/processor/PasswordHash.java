/*
 * IGI-2 Multiplayer Launcher Master Server
 *
 * Copyright (c) 2018, Aayush Atharva
 *
 * Permission is hereby granted, free of charge to any person obtaining a copy of this software and associated documentation files 
 * (the "Software"), to deal in the Software with restriction. A person can use, copy the Software without restriction. But if a person 
 * modify the software, the person must push the code to the Software GitHub repository. If a person wants to publish or distribute the 
 * software, the person must put this "Created By: Aayush Atharva" on About Section of the Software And this License must be present with 
 * every file of the Software. And If a person wants to sell the software, the person get permission from the owner of this Software.
 *
 *
 *
 *
 * IGI-2 Multiplayer Launcher Master Server
 * Owner: Aayush Atharva
 * Email: aayush@igi2.co.in
 */
package launcher.server.processor;

import aayush.atharva.TurboCryptography.Decryption;
import aayush.atharva.TurboCryptography.Encryption;
import java.math.BigInteger;
import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Hyper
 */
public class PasswordHash {

    public String HashPassword(String Password) throws Exception {
        return new PasswordHash().GetEncryptedHash(new PasswordHash().generateHash(new PasswordHash().EncryptPass(Password)));
    }

    public boolean isPasswordValid(String Password, String Hash) throws Exception {
        return new PasswordHash().validatePassword(new PasswordHash().EncryptPass(Password), new PasswordHash().DecryptHash(Hash));
    }

    private boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] testHash = skf.generateSecret(spec).getEncoded();
        int diff = hash.length ^ testHash.length;

        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }

        return diff == 0;
    }

    private String generateHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt().getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }

    private String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    private byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    private String EncryptPass(String Password) throws Exception {
        String Key = "%tH&koY3S8j9dxJ9";
        String KeyAES = "ECC216E42C23D21BF151A15C4A2EC83B";
        String IV = "6Sl%$d9uz6Z!&k0a";
        String CBCA = "^%bT6#B^O#Qq@4B5";
        String CBCB = "$Ao#45ZG%M6F7@z5";
        String AESSecretKey = "9MYG4lZ@XUZy4i9sXlb^0&%jq!bgQeYqTYUP45xKc#5&Oq87@NZfzY%931cc*R6pL6p0ekOitbwlJfRPeI0Te%0p2pVgVYN9J4TNtq1n3NlcxzZppCC9yfUK!nkBNH9TWQ%Xr0JDFNzHbcvnF9ET#ostlVufBDnnY0aLW2s8Y%6glp@xu3KvS2DUQFlp5JATz$h*YFC8";

        return new Encryption(AESSecretKey, CBCA, CBCB, KeyAES, Key, IV, Password).Encrypt();
    }

    private String GetEncryptedHash(String Hash) throws Exception {
        String Key = "^c2jZE*2a4@7wCiB";
        String KeyAES = "EEC32068078FCAFC42D5B33F9E2EB1F8";
        String IV = "1eAhP&#7rHQtNfn5";
        String CBCA = "$zX5qtSc1zcw2XYv";
        String CBCB = "k6!#5Jk1O*9K@0%t";
        String AESSecretKey = "48UdqoH@F7LgDHTAz@2&24dT$yB#xObaexIDZBi#wztLU!doW$E#OFUrKF%Xa@3raExsY4pfqkMI0jgbEz*@5F6s5DSU%o8C#JtSW9D0%4p*XNOI6ULjhg8q5y1M3os00tT3$ffffScrscsOBeAZ8k9webclS^s^LdLPU!Eet%BAQJQxbq*qSPwD!z@mer6VmdWUHHjI";
        return new Encryption(AESSecretKey, CBCA, CBCB, KeyAES, Key, IV, Hash).Encrypt();
    }

    private String DecryptPass(String Password) throws Exception {
        String Key = "%tH&koY3S8j9dxJ9";
        String KeyAES = "ECC216E42C23D21BF151A15C4A2EC83B";
        String IV = "6Sl%$d9uz6Z!&k0a";
        String CBCA = "^%bT6#B^O#Qq@4B5";
        String CBCB = "$Ao#45ZG%M6F7@z5";
        String AESSecretKey = "9MYG4lZ@XUZy4i9sXlb^0&%jq!bgQeYqTYUP45xKc#5&Oq87@NZfzY%931cc*R6pL6p0ekOitbwlJfRPeI0Te%0p2pVgVYN9J4TNtq1n3NlcxzZppCC9yfUK!nkBNH9TWQ%Xr0JDFNzHbcvnF9ET#ostlVufBDnnY0aLW2s8Y%6glp@xu3KvS2DUQFlp5JATz$h*YFC8";
        return new Decryption(AESSecretKey, CBCA, CBCB, KeyAES, Key, IV, Password).Decrypt();
    }

    private String DecryptHash(String Hash) throws Exception {
        String Key = "^c2jZE*2a4@7wCiB";
        String KeyAES = "EEC32068078FCAFC42D5B33F9E2EB1F8";
        String IV = "1eAhP&#7rHQtNfn5";
        String CBCA = "$zX5qtSc1zcw2XYv";
        String CBCB = "k6!#5Jk1O*9K@0%t";
        String AESSecretKey = "48UdqoH@F7LgDHTAz@2&24dT$yB#xObaexIDZBi#wztLU!doW$E#OFUrKF%Xa@3raExsY4pfqkMI0jgbEz*@5F6s5DSU%o8C#JtSW9D0%4p*XNOI6ULjhg8q5y1M3os00tT3$ffffScrscsOBeAZ8k9webclS^s^LdLPU!Eet%BAQJQxbq*qSPwD!z@mer6VmdWUHHjI";
        return new Decryption(AESSecretKey, CBCA, CBCB, KeyAES, Key, IV, Hash).Decrypt();
    }
}
