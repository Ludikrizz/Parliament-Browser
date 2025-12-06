package org.v_04_01.dataprocessing.helper;

import com.password4j.BcryptFunction;
import com.password4j.types.Bcrypt;

/**
 * Hilfsklasse zum Hashen und Verifizieren von Passwörtern
 * 
 */
public class Password {

    private BcryptFunction bcrypt = BcryptFunction.getInstance(Bcrypt.B, 12);

    private String pepper = "hEep098L3nx2-jk4Qg538bo";

    /**
     * Hashed ein Passwort
     * 
     * @param password das zu hashende Passwort
     * @return gibt das gehashte Passwort zurück
     */
    public String hash(String password) {
        return com.password4j.Password.hash(password)
                .addPepper(pepper)
                .with(bcrypt)
                .getResult();
    }

    /**
     * Verifiziert ein Passwort
     * 
     * @param password das zu verifizierende Passwort
     * @param hash     das gehashte Passwort
     * @return gibt zurück, ob das Passwort korrekt ist
     */
    public boolean verify(String password, String hash) {
        return com.password4j.Password.check(password, hash)
                .addPepper(pepper)
                .with(bcrypt);
    }
}
