package com.company;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.nio.ByteBuffer;

/**
 * Decrypter Class
 * Created by Haitham Athamneh
 */
public class Decrypter {
    private Cipher cipher;
    private int bsize;
    private SecretKey key;

    Decrypter(SecretKey key, AppConfig config) throws Exception {
        this.key = key;
        cipher = Cipher.getInstance(config.encType+"/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, this.key);
        bsize = config.blockSize;
    }

    /**
     * Decryption mode ECB
     * @param c Cipher to be decrypted
     * @return Decrypted message
     * @throws Exception if decryption fails
     */
    byte[] decrypt (byte[] c) throws Exception {
        return cipher.doFinal(c);
    }

    /**
     * Decryption mode CBC
     * @param c Cipher to be decrypted
     * @param initial current counter
     * @return Decrypted message
     * @throws Exception if decryption fails
     */
    byte[] decrypt (byte[] c, byte[] initial) throws Exception {
        byte[] dec = cipher.doFinal(c);
        //AppConfig.printArr(dec);
        byte[] msg = new byte[c.length];
        for (int i = 0; i < msg.length; i++) {
            msg[i] = (byte) (dec[i] ^ initial[i]);
        }
        return msg;
    }

    /**
     * Decryption mode CTR
     * @param c Cipher to be decrypted
     * @param counter current counter
     * @return Decrypted message
     * @throws Exception if decryption fails
     */
    byte[] decrypt (byte[] c, byte counter) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        ByteBuffer b = ByteBuffer.allocate(bsize);
        b.put(counter);
        byte[] ctrCipher = cipher.doFinal(b.array());
        byte[] res = new byte[c.length];
        for (int i = 0; i < c.length; i++) {
            res[i] = (byte) (c[i] ^ ctrCipher[i]);
        }
        return res;
    }

}
