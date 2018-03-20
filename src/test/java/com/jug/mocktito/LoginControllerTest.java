package com.jug.mocktito;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Created by blacktiago on 3/19/18.
 */
public class LoginControllerTest {

    private static final String USER = "santiago";
    private static final String PASSWORD = "securePASS123";

    private AuthenticatorInterface authenticatorMock;
    private DataProviderInterface dataProviderMock;
    private LoginController loginBean;

    @Before
    public void setUp() {
        loginBean = spy(new LoginController());
        authenticatorMock = mock(AuthenticatorInterface.class);
        loginBean.setAutenticator(authenticatorMock);

        dataProviderMock = mock(DataProviderInterface.class);
        loginBean.setDataProvider(dataProviderMock);
    }

    private Answer sucessAnswer = new Answer<HashMap<String, Object>>() {

        public HashMap<String, Object> answer(InvocationOnMock invocationOnMock) throws Throwable {
            HashMap<Integer, String> menu = new HashMap<Integer, String>();
            HashMap<String, Object> userInfo = new HashMap<String, Object>();

            menu.put(1, "Consolidado");
            menu.put(2, "Transferencias");
            menu.put(3, "Pagos");
            userInfo.put("MENU", menu);
            userInfo.put("LAST_LOGIN", "14/02/2018");

            return userInfo;
        }
    };

    @Test
    public void testLoginSucess() {
        when(authenticatorMock.login(USER, PASSWORD)).thenReturn(true);
        when(dataProviderMock.getUserInfo(USER)).thenAnswer(sucessAnswer);
        LoginResponse outcome = loginBean.login(USER, PASSWORD);

        InOrder inOrder = inOrder(authenticatorMock);
        inOrder.verify(authenticatorMock, atLeastOnce()).audit(USER, "BEGIN");
        inOrder.verify(authenticatorMock, times(1)).login(USER, PASSWORD);
        inOrder.verify(authenticatorMock, atLeast(1)).audit(USER, "SUCESS");

        verify(loginBean, times(1)).notifyEvent(USER, "SUCESS");

        Assert.assertEquals("Test Sucess login", true, outcome.isLoged());
        Assert.assertEquals("verify menu size", 2, outcome.getUserInfo().size());
    }

    private static ArgumentMatcher<String> invalidPassword = new ArgumentMatcher<String>() {
        public boolean matches(String s) {
            String regex = "[a-zA-Z0-9]*";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(s);
            return !matcher.matches();
        }
    };

    @Test(expected = IllegalArgumentException.class)
    public void failedLoginTest() {
        when(authenticatorMock.login(USER, PASSWORD)).thenReturn(true);
        //when(authenticatorMock.login(eq(USER), argThat(invalidPassword))).thenReturn(false);
        //when(authenticatorMock.login(eq(USER), argThat(invalidPassword))).thenThrow(new IllegalArgumentException());
        given(authenticatorMock.login(eq(USER), argThat(invalidPassword))).willThrow(new IllegalArgumentException());

        LoginResponse login = loginBean.login(USER, "Admin123@");
        Assert.assertEquals("Test fail login", false, login.isLoged());
    }
}
