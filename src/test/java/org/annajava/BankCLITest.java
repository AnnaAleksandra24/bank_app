package org.annajava;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class BankCLITest{

    @Test
    public void testRegistrationFlow(){
        String str = String.join("\n", "1", "anna", "3");

        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);

        BankCLI cli = new BankCLI(in, printStream, new PasswordGenerator());
        cli.run();

        String output = out.toString();
        assertTrue(output.contains("Welcome to the Bank system!"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    public void guestCanDoRegistrationAndLoginAfter(){
        String password = "password";
        String str = String.join("\n", "1", "anna", "2", "anna", password, "4");

        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);

        BankCLI cli = new BankCLI(in, printStream, new MockPasswordGenerator(password));
        cli.run();

        String output = out.toString();
        assertTrue(output.contains("✅ Welcome anna"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    public void guestCanDoRegistrationAndLoginAndHasZeroBalance(){
        String password = "password";
        String str = String.join("\n", "1", "anna", "2", "anna", password, "1", "4");

        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);

        BankCLI cli = new BankCLI(in, printStream, new MockPasswordGenerator(password));
        cli.run();

        String output = out.toString();
        assertTrue(output.contains("✅ Welcome anna"));
        assertTrue(output.contains("Your balance is: 0.0"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    public void userCanDoDeposit(){
        String password = "password";
        String str = String.join("\n", "1", "anna", "2", "anna", password, "2", "15000", "4");

        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);

        BankCLI cli = new BankCLI(in, printStream, new MockPasswordGenerator(password));
        cli.run();

        String output = out.toString();
        assertTrue(output.contains("✅ Welcome anna"));
        assertTrue(output.contains("Your deposit amount now: 15000.0"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    public void userCanDoWithdrawFlow(){
        String password = "password";
        String str = String.join("\n", "1", "anna", "2", "anna", password, "2", "15000", "3", "2560", "4");

        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);

        BankCLI cli = new BankCLI(in, printStream, new MockPasswordGenerator(password));
        cli.run();

        String output = out.toString();
        assertTrue(output.contains("✅ Welcome anna"));
        assertTrue(output.contains("Your deposit amount now: 15000.0"));
        assertTrue(output.contains("Your deposit amount now: 12440.0"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    public void userHasRightTransactionRecords(){
        String password = "password";
        String str = String.join("\n", "1", "anna", "2", "anna", password, "2", "15000", "3", "2560", "5", "4");

        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);

        BankCLI cli = new BankCLI(in, printStream, new MockPasswordGenerator(password));
        cli.run();

        String output = out.toString();
        assertTrue(output.contains("List of transactions for: anna"));
        assertTrue(output.contains(":\tDEPOSIT\t|\t0.0\t|\t0.0"));
        assertTrue(output.contains(":\tDEPOSIT\t|\t15000.0\t|\t15000.0"));
        assertTrue(output.contains(":\tWITHDRAW\t|\t2560.0\t|\t12440.0"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    public void userCannotDoWithdrawMoreMoneyThanHave(){
        String password = "password";
        String str = String.join("\n", "1", "anna", "2", "anna", password, "2", "15000", "3", "16000", "4");

        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);

        BankCLI cli = new BankCLI(in, printStream, new MockPasswordGenerator(password));
        cli.run();

        String output = out.toString();
        assertTrue(output.contains("✅ Welcome anna"));
        assertTrue(output.contains("Your deposit amount now: 15000.0"));
        assertTrue(output.contains("You cannot get money more than your account has!"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    public void gotAnInvalidCommand(){
        String password = "password";
        String str = String.join("\n", "1", "anna", "20", "4");

        ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);

        BankCLI cli = new BankCLI(in, printStream, new MockPasswordGenerator(password));
        cli.run();

        String output = out.toString();
        assertTrue(output.contains("\uD83D\uDD34 Invalid command"));
    }
}
