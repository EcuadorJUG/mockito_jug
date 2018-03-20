package com.jug.mocktito;

/**
 * Created by blacktiago on 3/19/18.
 */
public class LoginController {

    private AuthenticatorInterface autenticator;
    private DataProviderInterface dataProvider;

    public LoginResponse login(String user, String password) {
        LoginResponse outcome = new LoginResponse();
        this.autenticator.audit(user, "BEGIN");
        boolean sucess = this.autenticator.login(user, password);
        if (sucess) {
            outcome.setLoged(sucess);
            this.autenticator.audit(user, "SUCESS");
            this.notifyEvent(user,"SUCESS");
            outcome.setUserInfo(this.dataProvider.getUserInfo(user));
        }
        return outcome;
    }

    public void setAutenticator(AuthenticatorInterface autenticator) {
        this.autenticator = autenticator;
    }

    public void notifyEvent(String user, String sucess) {
    }

    public void setDataProvider(DataProviderInterface dataProvider) {
        this.dataProvider = dataProvider;
    }
}
