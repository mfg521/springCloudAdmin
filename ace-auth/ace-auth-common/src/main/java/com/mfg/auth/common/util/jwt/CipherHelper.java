package com.mfg.auth.common.util.jwt;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author someone
 * @create 2017-10-10 9:44
 **/
@Configuration
public class CipherHelper {

    //加密
    public static String encrypt(String encryptString) throws Exception {

        byte[] iv1 = {(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
        byte[] DESkey = "xxd34fo3".getBytes("UTF-8");
        IvParameterSpec iv = new IvParameterSpec(iv1);
        DESKeySpec dks = new DESKeySpec(DESkey);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return Base64.encode(cipher.doFinal(encryptString.getBytes()));
    }

    //解密
    public static String decrypt(String decryptString) throws Exception {
        byte[] DESkey = "xxd34fo3".getBytes("UTF-8");
        byte[] iv1 = {(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
        IvParameterSpec iv = new IvParameterSpec(iv1);
        SecretKeySpec key = new SecretKeySpec(DESkey, "DES");

        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, iv);

        return new String(cipher.doFinal(Base64.decode(decryptString)));

    }


    public static void main(String[] args) throws Exception {


        String s = encrypt("tangxiaoyu");
        System.out.println(s);
        //System.out.println(decrypt("XlmF+92ZmuudZG8TVqzCww=="));

    }

}