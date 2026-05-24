/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121_poe;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author Student
 */
public class Login {
     // User storage variables
    private String storedUsername;
    private String storedPassword;
    private String storedPhoneNumber;
    private String firstName;
    private String lastName;
    private boolean isLoggedIn;
    
    /**
     * Constructor for Login class
     */
    public Login() {
        this.storedUsername = "";
        this.storedPassword = "";
        this.storedPhoneNumber = "";
        this.firstName = "";
        this.lastName = "";
         this.isLoggedIn = false;
    }
    
    /**
     * Method to check if username contains underscore and is no more than 5 characters
     * @param username The username to validate
     * @return true if username is valid, false otherwise
     */
     public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }
    
    /**
     * Method to check password complexity requirements
     * @param password The password to validate
     * @return true if password meets all requirements, false otherwise
     */
    public boolean checkPasswordComplexity(String password) {
        boolean hasMinLength = password.length() >= 8;
        boolean hasCapital = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasNumber = Pattern.compile("[0-9]").matcher(password).find();
        boolean hasSpecial = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]").matcher(password).find();
        
        return hasMinLength && hasCapital && hasNumber && hasSpecial;
    }
    
    /**
     * Method to check if cell phone number contains international code (+27 for SA)
     * @param phoneNumber The phone number to validate
     * @return true if phone number is valid, false otherwise
     */
    public boolean checkCellPhoneNumber(String phoneNumber) {
        boolean hasInternationalCode = phoneNumber.startsWith("+27");
        boolean validLength = false;
        
        if (hasInternationalCode) {
            String remaining = phoneNumber.substring(3);
            validLength = remaining.length() == 9 && remaining.matches("\\d+");
        }
        
        return hasInternationalCode && validLength;
    }
    
    /**
     * Method to register a new user with validation
     * @return Appropriate registration message
     */
    public String registerUser(String username, String password, String phoneNumber, String firstName, String lastName) {
        boolean isUsernameValid = checkUserName(username);
        boolean isPasswordValid = checkPasswordComplexity(password);
        boolean isPhoneValid = checkCellPhoneNumber(phoneNumber);
        
        if (!isUsernameValid) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        
        if (!isPasswordValid) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        
        if (!isPhoneValid) {
            return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        }
        
        this.storedUsername = username;
        this.storedPassword = password;
        this.storedPhoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        
        return "User successfully registered!";
    }
    
    /**
     * Method to verify login credentials
     * @return true if credentials match stored values, false otherwise
     */
    public boolean loginUser(String username, String password) {
        boolean success = storedUsername.equals(username) && storedPassword.equals(password);
        if (success) {
            isLoggedIn = true;
        }
        return success;
    }
    
    /**
     * Method to return login status message
     * @return Appropriate login message
     */
    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
    
    /**
     * Method to check if user is logged in
     * @return true if logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return isLoggedIn;
    }
    
    /**
     * Method to logout user
     */
    public void logout() {
        isLoggedIn = false;
    }
    
    // Getters for testing purposes
    public String getStoredUsername() { return storedUsername; }
    public String getStoredPassword() { return storedPassword; }
    public String getStoredPhoneNumber() { return storedPhoneNumber; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

}
