package com.company;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * Created by haitham on 28/11/16.
 */
public class Keygen {
    private SecureRandom secureRandom = new SecureRandom();
    private byte[] values;
    private String[] config;

    int keySize;

    Keygen(String[] args) {

        config = new String[3];
        config[1] = "f";
        config[2] = "key.txt";
        try {
            if(args.length == 0){
                System.out.println("Please Select key type ( -DES or -AES )");
                System.exit(0);
            }
            for (int i = 0; i < args.length; i++) {
                if(args[i].toLowerCase().equals("-des") || args[i].toLowerCase().equals("-aes")) {
                    config[0] = args[i].toLowerCase();
                } else {
                    switch (args[i].toLowerCase()){
                        case "-p":
                            config[1] = "p";
                            break;
                        case "-o":
                            config[2] = args[++i];
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }

        if(config[0].equals("-des")) keySize = 8;
        else if (config[0].equals("-aes")) keySize = 16;
        else {
            System.out.println("Please specify key type, -DES or -AES");
            System.exit(0);
        }


        values = new byte[keySize];
        secureRandom.nextBytes(values);
    }

    public byte[] getValues() {
        return values;
    }

    String getGeneratedKey() {
        String key = "";
        for (int i = 0; i < values.length; i++) {
            key +=  String.format("%02X",values[i]);
        }
        return key;
    }

    void generateNewKey() {
        values = new byte[keySize];
        secureRandom.nextBytes(values);
    }

    void saveKey() {
        String key = getGeneratedKey();
        if(config[1].equals("p")) {
            System.out.println(key);
        } else {
            try {
                FileOutputStream os = new FileOutputStream(config[2]);
                os.write(key.getBytes());
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
