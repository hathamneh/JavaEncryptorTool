package com.company;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.nio.ByteBuffer;

/**
 * Created by haitham on 28/11/16.
 */
public class Encrypter {

    private Cipher cipher;
    private int bsize;

    Encrypter(SecretKey key, AppConfig config) throws Exception {
        cipher = Cipher.getInstance(config.encType+"/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        bsize = config.blockSize;
    }

    /**
     * ECB mode encryption
     * @param msg msg to encrypt
     * @return cipher bytes array
     * @throws Exception when error happens
     */
    byte[] encrypt (byte[] msg) throws Exception {
        return cipher.doFinal(msg);
    }

    /**
     * CBC mode encryption
     * @param msg msg to encrypt
     * @param initial initial value for CBC
     * @return cipher bytes array
     * @throws Exception when error happens
     */
    byte[] encrypt (byte[] msg, byte[] initial) throws Exception {
        byte[] toEnc = new byte[msg.length];
        for (int i = 0; i < msg.length; i++) {
            toEnc[i] = (byte) (msg[i] ^ initial[i]);
        }
        return cipher.doFinal(toEnc);
    }

    /**
     * CTR mode encryption
     * @param msg msg to encrypt
     * @param counter counter for CTR Mode
     * @return cipher bytes array
     * @throws Exception when error happens
     */
    byte[] encrypt (byte[] msg, byte counter) throws Exception {
        ByteBuffer b = ByteBuffer.allocate(bsize);
        b.put(counter);
        byte[] ctrCipher = cipher.doFinal(b.array());
        byte[] res = new byte[msg.length];
        for (int i = 0; i < msg.length; i++) {
            res[i] = (byte) (msg[i] ^ ctrCipher[i]);
        }
        return res;
    }
}
