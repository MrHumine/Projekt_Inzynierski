package com.example.inzynierskiprojekt;

import static org.junit.Assert.*;
import org.junit.Test;

public class IntentRegisterTest {

    @Test
    public void isPasswordEqualTest(){

    String password = "Haslo1";
    String passswordRe = "Haslo1";
    String passswordWrong = "Haslo2";

    assertTrue(IntentRegister.isPasswordEqual(password, passswordRe));

    assertFalse(IntentRegister.isPasswordEqual(password, passswordWrong));
    }

    @Test
    public void isEmailAndPasswordEmptyTest(){

    String email = "test@test.pl";
    String password = "testPassword1";
    String emailEmpty = "";
    String passwordEmpty = "";

    assertTrue(IntentRegister.isEmailAndPasswordEmpty(email, password));

    assertFalse(IntentRegister.isEmailAndPasswordEmpty(emailEmpty, password));
    assertFalse(IntentRegister.isEmailAndPasswordEmpty(emailEmpty, passwordEmpty));
    assertFalse(IntentRegister.isEmailAndPasswordEmpty(email, passwordEmpty));
    }

}