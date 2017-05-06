package com.company;

import javax.crypto.SecretKey;

public class Main {

    public static void main(String[] args) {

        // Decode Arguments and initialize appConfig
        AppConfig appConfig = new DecConfig(args);

        // get the key ( key path: appConfig.keyPath )
        SecretKey key = appConfig.getKey();

        // get cipher and make it blocks ( msg path: appConfig.ctextPath )
        byte[][] blocks = appConfig.getBlocks();

        // Encryption happens here
        String decryptedMsg = "";
        try {
            Decrypter decrypter = new Decrypter(key, appConfig);
            byte[] s;
            byte[] c = null;
            byte counter = (byte)1;
            for (byte[] block :
                    blocks) {
                switch (appConfig.mode) {
                    case "ecb":
                        s = decrypter.decrypt(block);
                        break;
                    case "cbc":
                        if(c == null) c = appConfig.initalValue.getBytes();
                        s = decrypter.decrypt(block, c);
                        c = block;
                        break;
                    case "ctr":
                        s = decrypter.decrypt(block, counter++);
                        break;
                    default:
                        s = decrypter.decrypt(block);
                }

                for (byte x: s) {
                    // make cipher as hex
                    decryptedMsg += (char) x;
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        appConfig.saveOut(decryptedMsg);
    }
}
