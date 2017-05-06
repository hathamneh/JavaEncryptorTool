package com.company;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by haitham on 03/12/16.
 */
public class DecConfig extends AppConfig{
    private boolean saveToFile = false;

    DecConfig(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i].toLowerCase()) {
                case "-des":
                    encType = "DES";
                    blockSize = 8;
                    initalValue = "S8*4OEN/";
                    break;
                case "-aes":
                    encType = "AES";
                    blockSize = 16;
                    initalValue = "S8*4OEN/6Z3RKVBW";
                    break;
                case "-ecb":
                    mode = "ecb";
                    break;
                case "-cbc":
                    mode = "cbc";
                    break;
                case "-ctr":
                    mode = "ctr";
                    break;
                case "-key":
                    keyPath = args[++i];
                    break;
                case "-cipher":
                    ctextPath = args[++i];
                    break;
                case "-out":
                    saveToFile = true;
                    msg = args[++i];
                    break;
            }
        }
        if(encType == null) {
            System.err.println("Please specify cipher type ( -DES or -AES ).");
            System.exit(0);
        }
        if(mode == null) {
            System.err.println("Please specify blocks mode ( -ECB, -CBC or -CTR ).");
            System.exit(0);
        }

    }

    byte[][] getBlocks() {
        InputStream is = null;
        try {
            is = new FileInputStream(ctextPath);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }

        byte[][] blocks = null;
        try {
            int numberOfBlocks = (int) Math.ceil( ( (double)is.available()/2 ) / blockSize);
            blocks = new byte[numberOfBlocks][blockSize];
            byte[] tmpCipherBytes = new byte[is.available()];
            is.read(tmpCipherBytes,0,is.available());
            byte[] CipherBytes = hexStringToByteArray(tmpCipherBytes);
            for (int i = 0; i < numberOfBlocks; i++) {
                for (int j = 0; j < blockSize; j++) {
                    blocks[i][j] = CipherBytes[j+i*blockSize];
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return blocks;
    }

    void saveOut(String msg) {
        System.out.println(msg);
        if(saveToFile) {
            try {
                OutputStream os = new FileOutputStream(this.msg);
                os.write(msg.getBytes());
                os.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    SecretKey getKey() {
        try {
            InputStream is = new FileInputStream(keyPath);

            byte[] tmpkeyBytes = new byte[is.available()];
            is.read(tmpkeyBytes,0,is.available());

            byte[] keyBytes = hexStringToByteArray(tmpkeyBytes);

            SecretKey key = new SecretKeySpec(keyBytes, 0, keyBytes.length, encType);
            return key;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            //System.out.println("key.txt file not found, please check that is in the same folder with encrypt.");
            System.exit(0);
        }
        return null;
    }
}
