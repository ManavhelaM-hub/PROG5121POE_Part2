/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121_poe;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author Student
 */
public class LoginTest {
        // ── Test user used across login tests ─────────
    private Login createTestUser() {
        return new Login(
            "John", "Doe",
            "joh_1", "Pass@word123!",
            "+27712345678"
        );
    }

    // ═════════════════════════════════════════════
    // USERNAME TESTS
    // ═════════════════════════════════════════════

    @Test
    public void verifyUsernameIsValid() {
        Login user = new Login(
            "John", "Doe",
            "joh_1", "Pass@word123!",
            "+27712345678"
        );
        assertTrue(
            "joh_1 should pass: has underscore and is under 5 chars",
            user.checkUserName()
        );
    }

    @Test
    public void verifyUsernameIsInvalid() {
        Login user = new Login(
            "John", "Doe",
            "johnnytoolong", "Pass@word123!",
            "+27712345678"
        );
        assertFalse(
            "johnnytoolong should fail: no underscore and too long",
            user.checkUserName()
        );
    }

    // ═════════════════════════════════════════════
    // PASSWORD TESTS
    // ═════════════════════════════════════════════

    @Test
    public void verifyPasswordIsStrong() {
        Login user = new Login(
            "John", "Doe",
            "joh_1", "Pass@word123!",
            "+27712345678"
        );
        assertTrue(
            "Pass@word123! should pass all complexity rules",
            user.checkPasswordComplexity()
        );
    }

    @Test
    public void verifyPasswordIsWeak() {
        Login user = new Login(
            "John", "Doe",
            "joh_1", "simple",
            "+27712345678"
        );
        assertFalse(
            "simple should fail: too short and missing requirements",
            user.checkPasswordComplexity()
        );
    }

    // ═════════════════════════════════════════════
    // CELLPHONE TESTS
    // ═════════════════════════════════════════════

    @Test
    public void verifyCellNumberIsValid() {
        Login user = new Login(
            "John", "Doe",
            "joh_1", "Pass@word123!",
            "+27712345678"
        );
        assertTrue(
            "+27712345678 should pass: has international code",
            user.checkCellPhoneNumber()
        );
    }

    @Test
    public void verifyCellNumberIsInvalid() {
        Login user = new Login(
            "John", "Doe",
            "joh_1", "Pass@word123!",
            "0712345678"
        );
        assertFalse(
            "0712345678 should fail: missing international code",
            user.checkCellPhoneNumber()
        );
    }

    // ═════════════════════════════════════════════
    // LOGIN TESTS
    // ═════════════════════════════════════════════

    @Test
    public void verifyLoginPasses() {
        Login user = createTestUser();
        assertTrue(
            "Login should pass with correct credentials",
            user.loginUser("joh_1", "Pass@word123!")
        );
    }

    @Test
    public void verifyLoginFails() {
        Login user = createTestUser();
        assertFalse(
            "Login should fail with wrong password",
            user.loginUser("joh_1", "wrongpassword")
        );
    }

    // ═════════════════════════════════════════════
    // LOGIN STATUS MESSAGE TESTS
    // ═════════════════════════════════════════════

    @Test
    public void verifyWelcomeMessage() {
        Login user = createTestUser();
        String result = user.returnLoginStatus(
            "joh_1", "Pass@word123!"
        );
        assertEquals(
            "Welcome John Doe it is great to see you.",
            result
        );
    }

    @Test
    public void verifyErrorMessage() {
        Login user = createTestUser();
        String result = user.returnLoginStatus(
            "joh_1", "wrongpassword"
        );
        assertEquals(
            "Username or password incorrect, please try again.",
            result
        );
    }
}
