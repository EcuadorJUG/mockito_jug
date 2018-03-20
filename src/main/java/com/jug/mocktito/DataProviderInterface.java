package com.jug.mocktito;

import java.util.Map;

/**
 * Created by blacktiago on 3/19/18.
 */
public interface DataProviderInterface {
    Map<String,Object> getUserInfo(String user);
}
