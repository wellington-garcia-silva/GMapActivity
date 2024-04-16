package com.example.mylibrary;

//import com.google.gson.Gson;
import android.os.Build;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
//import java.security.NoSuchPaddingException;
import java.security.InvalidKeyException;
//import java.security.InvalidParameterSpecException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Cryptography extends Thread{

    private static final String KEY = "your_secret_key";

    public Cryptography(){
        start();
    }

    @Override
    public void run(){
        while(true) {
            //AdicionaNaFila();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static String encrypt(String data) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        }
        return data;//modifiquei aqui, colocando esse if e esse data de retorno
    }
    public static String decrypt(String encryptedData) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedData)), StandardCharsets.UTF_8);
        }
        return encryptedData;//modifiquei aqui, colocando esse if e esse data de retorno
    }
    public static Region encryptRegion(Region region) throws Exception {
        //SecretKey key = generateKey();
        //regionToJson(region);
        String encryptedName = new String(encrypt( KEY));
        double encryptedLatitude = region.getLatitude(); // Exemplo simplificado, você pode criptografar se necessário
        double encryptedLongitude = region.getLongitude(); // Exemplo simplificado, você pode criptografar se necessário
        int encryptedUser = region.getUser(); // Exemplo simplificado, você pode criptografar se necessário
        long encryptedTimestamp = region.getTimestamp();

        return new Region(encryptedName, encryptedLatitude, encryptedLongitude, encryptedUser, encryptedTimestamp);
    }
    public static Region decryptRegion(Region encryptedRegion) throws Exception {
        String decryptedName = decrypt(encryptedRegion.getName());
        double decryptedLatitude = encryptedRegion.getLatitude(); // A latitude já está descriptografada na classe Region original
        double decryptedLongitude = encryptedRegion.getLongitude(); // A longitude já está descriptografada na classe Region original
        int decryptedUser = encryptedRegion.getUser(); // O usuário já está descriptografado na classe Region original
        long decryptedTimestamp = encryptedRegion.getTimestamp();

        return new Region(decryptedName, decryptedLatitude, decryptedLongitude, decryptedUser, decryptedTimestamp);
    }
    /*public static String regionToJson(Region region) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(region);
    }*/
}

