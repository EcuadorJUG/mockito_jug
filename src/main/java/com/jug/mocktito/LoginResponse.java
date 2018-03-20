package com.jug.mocktito;

import java.util.BitSet;
import java.util.Map;

/**
 * Created by blacktiago on 3/19/18.
 */
public class LoginResponse {

    private boolean loged;
    private Map<String, Object> userInfo;

    public boolean isLoged() {
        return loged;
    }

    public void setLoged(boolean loged) {
        this.loged = loged;
    }

    public Map<String,Object> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Map<String,Object> userInfo) {
        this.userInfo = userInfo;
    }
}
