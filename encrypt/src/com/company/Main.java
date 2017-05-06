package com.company;

import javax.crypto.SecretKey;

public class Main {

    public static void main(String[] args) {

        // Decode Arguments and initialize encConfig
        AppConfig encConfig = new EncConfig(args);

        // get the key ( key path: encConfig.keyPath )
        SecretKey key = encConfig.getKey();

        // get the message and make it blocks ( msg path: encConfig.msgPath )
        byte[][] blocks = encConfig.getBlocks();


        // Encryption happens here
        String cipher = "";
        try {
            Encrypter encrypter = new Encrypter(key, encConfig);
            byte[] s = null;
            byte counter = (byte)1;
            for (byte[] block :
                    blocks) {
                switch (encConfig.mode) {
                    case "ecb":
                        s = encrypter.encrypt(block);
                        break;
                    case "cbc":
                        if(s == null) s = encConfig.initalValue.getBytes();
                        s = encrypter.encrypt(block, s);
                        break;
                    case "ctr":
                        s = encrypter.encrypt(block, counter++);
                        break;
                    default:
                        s = encrypter.encrypt(block);
                }

                for (byte x: s) {
                    // make cipher as hex
                    cipher += String.format("%02X", x);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // save cipher to ctext.txt
        encConfig.saveOut(cipher);

    }



}
