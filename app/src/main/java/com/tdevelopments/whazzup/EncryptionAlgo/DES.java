package com.tdevelopments.whazzup.EncryptionAlgo;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DES<myEncKey> {




    //alll the stuff for the key generation
    private static final String UNICODE_FORMAT="UTF8";
    public static final String DES_ENCRYPTION_SCHEME="DES";
    private KeySpec myKeySpec;
    private SecretKeyFactory mySecretKeyFactory;
    private Cipher cipher;
    byte[] KeyAsBytes;
    private String myEncryptionKey;
    private String getMyEncryptionScheme;
    SecretKey key;
    String myEncKey="This is Key";

    String dummy="welcome to the world of java";
    String ans="";







    public String encrypt(String unencryptedString)
    {
        String encryptedString =null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);

            encryptedString=Base64.encodeToString(encryptedText, Base64.DEFAULT);
        }
        catch (InvalidKeyException|UnsupportedEncodingException|IllegalBlockSizeException |BadPaddingException e) {

        }

        return encryptedString;
    }

    public String decrypt(String encrytedString)
    { String decryptedText = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decode(encrytedString, Base64.DEFAULT);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText = bytes2String(plainText);
        }catch(InvalidKeyException|IllegalBlockSizeException|BadPaddingException e) {

        }
        return decryptedText;
    }

    //convert a byte[] into a string
    private static String bytes2String(byte[] bytes)
    {
        StringBuilder stringBuffer=new StringBuilder();
        for(int i=0;i<bytes.length;i++)
        {
            stringBuffer.append((char) bytes[i]);

        }
        return stringBuffer.toString();
    }






}
