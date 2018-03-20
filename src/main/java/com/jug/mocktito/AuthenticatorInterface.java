package com.jug.mocktito;

/**
 * Created by blacktiago on 3/19/18.
 */
public interface AuthenticatorInterface {
    boolean login(String user, String password);

    void audit(String user, String begin);
}
