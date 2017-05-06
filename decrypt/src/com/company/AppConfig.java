package com.company;

import javax.crypto.SecretKey;

/**
 * Created by haitham on 28/11/16.
 */
public abstract class AppConfig {
    String encType = null;
    String mode = null;
    int blockSize = 0;
    String keyPath = "key.txt";
    String msg = "msg.txt";
    String ctextPath = "ctext.txt";

    String initalValue;

    abstract SecretKey getKey();
    abstract void saveOut(String s);
    abstract byte[][] getBlocks();

    byte[] hexStringToByteArray(byte[] bytes) {
        String s = new String(bytes);
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    static void printArr(byte[] arr) {
        for (byte b :
                arr) {
            System.out.printf("%02X",b);
        }
        System.out.println();
    }
}
