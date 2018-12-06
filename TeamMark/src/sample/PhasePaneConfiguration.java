package sample;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class PhasePaneConfiguration implements Serializable {

    private static final int NUMBER_OF_PINS_PER_PHASE = 16;
    private boolean[] pins = new boolean[NUMBER_OF_PINS_PER_PHASE];
    private byte[] encryptedPassword;
    private byte[] salt = new byte[8];

    public PhasePaneConfiguration(boolean[] pinConfig) {
        if(pinConfig.length != NUMBER_OF_PINS_PER_PHASE) {
            throw new IllegalStateException("Invalid pin configuration size.");
        }
        for(int i = 0; i < NUMBER_OF_PINS_PER_PHASE; i++) {
            pins[i] = pinConfig[i];
        }
    }

    public void savePassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if(encryptedPassword != null) {
            throw new IllegalStateException("Phase Configuration already has password associated it.");
        }
        encryptedPassword = encrypt(password);
    }

    public boolean authenticate(String attemptedPassword) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if(encryptedPassword == null) {
            throw new IllegalStateException("No password set for Phase Configuration");
        }
        return Arrays.equals(encryptedPassword, encrypt(attemptedPassword));
    }

    private byte[] encrypt(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if(salt == null) {
            SecureRandom.getInstance("SHA1PRNG").nextBytes(salt);
        }

        //SHA-1 generates 160 bit hashes, which explains the last keyLength parameter
        KeySpec ks = new PBEKeySpec(password.toCharArray(), salt, 25000, 160);

        return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(ks).getEncoded();
    }

    public boolean[] getPins() {
        return pins;
    }
}
