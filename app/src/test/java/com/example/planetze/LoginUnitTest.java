package com.example.planetze;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.MockitoJUnitRunner;


import static org.junit.Assert.*;

import androidx.fragment.app.Fragment;

import com.example.planetze.Login.LoginModel;
import com.example.planetze.Login.LoginPresenter;
import com.example.planetze.Login.LoginView;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginUnitTest {
    @Mock
    LoginModel model;
    @Mock
    LoginView view;



    @Test
    public void testNullEmail() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser(null, "password123");
        verify(view).setMessage("Email cannot be empty");

    }

    @Test
    public void testNullPassword() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("jiangminki0@gmail.com", null);
        verify(view).setMessage("Email cannot be empty");

    }

    @Test
    public void testEmptyEmail() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("", "password123");
        verify(view).setMessage("Email cannot be empty");

    }

    @Test
    public void testEmptyPassword() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("jiangminki0@gmail.com", "");
        verify(view).setMessage("Password cannot be empty");
    }

    @Test
    public void testEmptyEmailWithSpace() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("     ", "password123");
        verify(view).setMessage("Email cannot be empty");

    }

    @Test
    public void testEmptyPasswordWithSpace() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("jiangminki0@gmail.com", "       ");
        verify(view).setMessage("Password cannot be empty");
    }

    @Test
    public void testWrongEmailRightPassword() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("wrongemail@gmail.com", "hellohello");
        verify(view).setMessage("Invalid email or password");
    }

    @Test
    public void testRightEmailWrongPassword() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("jiangminki0@gmail.com", "wrongpassword");
        verify(view).setMessage("Invalid email or password");
    }

    @Test
    public void testWrongEmailWrongPassword() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("wrongemail@gmail.com", "wrongpassword");
        verify(view).setMessage("Invalid email or password");
    }

    @Test
    public void testRightEmailRightPassword() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("jiangminki0@gmail.com", "hellohello");
        verify(view).setMessage("Login sucessful");
    }

    @Test
    public void testSuccessfulLogin() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.loginUser("jiangminki0@gmail.com", "hellohello");
        verify(view).takeToHomePage();
    }

    @Test
    public void testMessage() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.setMessage("this is a message");
        verify(view).setMessage("this is a message");
    }

    @Test
    public void testTakeToHomePage() {
        LoginPresenter presenter = new LoginPresenter(model,view);
        presenter.takeToHomePage();
        verify(view).takeToHomePage();
    }




}