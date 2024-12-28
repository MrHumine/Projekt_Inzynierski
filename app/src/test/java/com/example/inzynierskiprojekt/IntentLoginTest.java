package com.example.inzynierskiprojekt;

import static org.junit.Assert.*;

import org.junit.Test;

public class IntentLoginTest {

    @Test
    public void isEmailOrPasswordEmptyTest(){
    String email = "test@test.pl";
    String password = "TestoweHaslo1";
    String emailEmpty = "";
    String passwordEmpty = "";

    assertFalse(IntentLogin.isEmailOrPasswordEmpty(email, password));
    assertTrue(IntentLogin.isEmailOrPasswordEmpty(email, passwordEmpty));
    assertTrue(IntentLogin.isEmailOrPasswordEmpty(emailEmpty, password));

    assertTrue(IntentLogin.isEmailOrPasswordEmpty(emailEmpty, passwordEmpty));
    }
}