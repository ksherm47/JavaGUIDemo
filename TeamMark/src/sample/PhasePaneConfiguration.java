package sample;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class PhasePaneConfiguration {

    private static final int NUMBER_OF_PINS_PER_PHASE = 16;
    private boolean[] pins = new boolean[NUMBER_OF_PINS_PER_PHASE];

    public PhasePaneConfiguration(boolean[] pinConfig) {
        if(pinConfig.length != NUMBER_OF_PINS_PER_PHASE) {
            throw new IllegalStateException("Invalid pin configuration size.");
        }
        for(int i = 0; i < NUMBER_OF_PINS_PER_PHASE; i++) {
            pins[i] = pinConfig[i];
        }
    }

    public boolean[] getPins() {
        return pins;
    }
}
