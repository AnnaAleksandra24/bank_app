package org.annajava;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class BankTest {

    @Test
    public void testRegisterSuccess(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        Bank bank = new Bank(new MockPasswordGenerator("1234"), printStream);
        boolean status = bank.register("user");
        assertTrue(status);
    }

    @Test
    public void testRegisterFail(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        Bank bank = new Bank(new MockPasswordGenerator("1234"), printStream);
        bank.register("user");
        boolean status = bank.register("user");
        assertFalse(status);
    }
//
    @Test
    public void testLoginSuccess(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        Bank bank = new Bank(new MockPasswordGenerator("1234"), printStream);
        bank.register("user");
        BankAccount account = bank.login("user", "1234");
        assertNotNull(account);
    }

    @Test
    public void testLoginFail(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        Bank bank = new Bank(new MockPasswordGenerator("1234"), printStream);
        bank.register("user");
        BankAccount account = bank.login("user", "12345");
        assertNull(account);
    }
}
