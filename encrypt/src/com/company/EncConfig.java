package com.company;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by haitham on 28/11/16.
 */
public class EncConfig extends AppConfig{
    private boolean echo = false;
    private boolean fromFile = true;
    private boolean inline = false;

    // TODO: 28/11/16 make initial value can be set from arguments

    EncConfig(String[] args) {
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
                case "-i":
                    inline = true;
                    fromFile = false;
                    msg = args[++i];
                    break;
                case "-m":
                    msg = args[++i];
                    break;
                case "-out":
                    ctextPath = args[++i];
                    break;
                case "-print":
                    echo = true;
            }
        }
        if (encType == null) {
            System.err.println("Please specify cipher type ( -DES or -AES ).");
            System.exit(0);
        }
        if (mode == null) {
            System.err.println("Please specify blocks mode ( -ECB, -CBC or -CTR ).");
            System.exit(0);
        }
        if(inline && fromFile) {
            System.err.println("You can't choose encryption from file (-m) and inline (-i) together!");
            System.exit(0);
        }
    }

    SecretKey getKey() {
        try {
            InputStream is = new FileInputStream(keyPath);

            byte[] tmpkeyBytes = new byte[is.available()];
            is.read(tmpkeyBytes, 0, is.available());

            byte[] keyBytes = hexStringToByteArray(tmpkeyBytes);

            SecretKey key = new SecretKeySpec(keyBytes, 0, keyBytes.length, encType);
            return key;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            //System.out.println(keyPath+" file not found, please check that is in the same folder with encrypt.");
            System.exit(0);
        }
        return null;
    }

    byte[][] getBlocks() {
        byte[][] blocks = null;
        int numberOfBlocks;
        boolean notMultiple = false;
        if(fromFile) {
            InputStream is = null;
            try {
                is = new FileInputStream(msg);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }
            try {
                if(is.available() % blockSize != 0)
                    notMultiple = true;

                numberOfBlocks = (int) Math.ceil(((double) is.available()) / blockSize);
                blocks = new byte[numberOfBlocks][blockSize];
                int i = 0;
                while (is.available() != 0) {
                    is.read(blocks[i++], 0, blockSize);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else if (inline) {
            byte[] inlineBytes = msg.getBytes();

            if(inlineBytes.length % blockSize != 0)
                notMultiple = true;

            numberOfBlocks = (int) Math.ceil(((double) inlineBytes.length) / blockSize);
            blocks = new byte[numberOfBlocks][blockSize];
            for (int i = 0; i < numberOfBlocks; i++) {
                for (int j = 0; j < blockSize; j++) {
                    int ind = j + blockSize*i;
                    if( ind >= inlineBytes.length )
                        break;
                    blocks[i][j] = inlineBytes[ind];
                }
            }
        }
        if(notMultiple)
            System.err.println("Warning: Your message is not multiple of block size, padding with zeros will be made.");
        return blocks;
    }

    void saveOut(String cipher) {
        OutputStream os = null;

        if(echo) {
            System.out.println(cipher);
        }
        try {
            os = new FileOutputStream(ctextPath);
            os.write(cipher.getBytes());
            os.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
