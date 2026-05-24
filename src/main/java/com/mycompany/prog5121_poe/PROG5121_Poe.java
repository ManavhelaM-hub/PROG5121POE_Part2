/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.prog5121_poe;
import java.util.Scanner;
import org.json.JSONException;

/**
 *
 * @author Student
 */
public class PROG5121_Poe {
private static Login loginSystem;
    private static Message messageSystem;
    private static Scanner scanner;
    private static int numMessages;


    public static void main(String[] args) throws JSONException {
      scanner = new Scanner(System.in);
        loginSystem = new Login();
        messageSystem = new Message();
        
        System.out.println("=".repeat(60));
        System.out.println("     WELCOME TO QUICKCHAT");
        System.out.println("=".repeat(60));
        System.out.println();
        
        // Load any existing messages from JSON
        System.out.println(messageSystem.loadMessagesFromJSON("messages.json"));
        System.out.println();
        
        // Registration Process
        if (!performRegistration()) {
            System.out.println("\n✗ Registration failed. Please restart the application.");
            scanner.close();
            return;
        }
        
        // Login Process
        if (!performLogin()) {
            System.out.println("\n✗ Too many failed login attempts. Exiting application.");
            scanner.close();
            return;
        }
        
        // Ask how many messages they want to enter
        System.out.print("\nHow many messages would you like to compose? ");
        numMessages = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        // Main application menu
        runMessagingApp();
        
        scanner.close();
        System.out.println("\nThank you for using QuickChat!");
    }
    
    /**
     * Perform user registration
     * @return true if registration successful, false otherwise
     */
    private static boolean performRegistration() {
        System.out.println("REGISTRATION REQUIREMENTS:");
        System.out.println("-".repeat(50));
        System.out.println("Username: Must contain '_' and be ≤ 5 characters");
        System.out.println("Password: 8+ chars, 1 capital, 1 number, 1 special char");
        System.out.println("Phone: +27 followed by 9 digits (e.g., +27831234567)");
        System.out.println("-".repeat(50));
        
        System.out.println("\nLet's create your account!");
        
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();
        
        String username;
        boolean usernameValid = false;
        do {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            if (!loginSystem.checkUserName(username)) {
                System.out.println(" Username must contain '_' and be ≤ 5 characters");
            } else {
                usernameValid = true;
            }
        } while (!usernameValid);
        
        String password;
        boolean passwordValid = false;
        do {
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            if (!loginSystem.checkPasswordComplexity(password)) {
                System.out.println(" Password must have 8+ chars, 1 capital, 1 number, 1 special char");
            } else {
                passwordValid = true;
            }
        } while (!passwordValid);
        
        String phoneNumber;
        boolean phoneValid = false;
        do {
            System.out.print("Enter SA cell number (+27xxxxxxxxx): ");
            phoneNumber = scanner.nextLine();
            if (!loginSystem.checkCellPhoneNumber(phoneNumber)) {
                System.out.println(" Must start with +27 followed by 9 digits");
            } else {
                phoneValid = true;
            }
        } while (!phoneValid);
        
        String registrationResult = loginSystem.registerUser(username, password, phoneNumber, firstName, lastName);
        System.out.println("\n" + registrationResult);
        
        return registrationResult.equals("User successfully registered!");
    }
    
    /**
     * Perform user login
     * @return true if login successful, false otherwise
     */
    private static boolean performLogin() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("     LOGIN TO YOUR ACCOUNT");
        System.out.println("=".repeat(60));
        
        int attempts = 0;
        while (attempts < 3) {
            System.out.println("\nLogin Attempt " + (attempts + 1) + " of 3");
            System.out.print("Username: ");
            String loginUsername = scanner.nextLine();
            System.out.print("Password: ");
            String loginPassword = scanner.nextLine();
            
            String loginStatus = loginSystem.returnLoginStatus(loginUsername, loginPassword);
            System.out.println(loginStatus);
            
            if (loginSystem.isLoggedIn()) {
                System.out.println("\n✓ Login successful! Welcome to QuickChat!");
                return true;
            }
            attempts++;
        }
        return false;
    }
    
    /**
     * Run the main messaging application
     */
    private static void runMessagingApp() throws JSONException {
        boolean running = true;
        
        while (running) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("     QUICKCHAT MAIN MENU");
            System.out.println("=".repeat(50));
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.print("\nChoose option (1-3): ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    sendMessages();
                    break;
                case 2:
                    System.out.println("\n🔜 Coming Soon. This feature is still in development.");
                    break;
                case 3:
                    // Save messages to JSON before quitting
                    System.out.println("\n" + messageSystem.storeMessagesInJSON("messages.json"));
                    System.out.println(messageSystem.getTotalMessagesDisplay());
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }
    }
    
    /**
     * Send multiple messages based on user-defined count
     */
    private static void sendMessages() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("     SEND MESSAGES");
        System.out.println("=".repeat(50));
        System.out.println("You can compose up to " + numMessages + " message(s).\n");
        
        for (int i = 1; i <= numMessages; i++) {
            System.out.println("\n--- Message " + i + " of " + numMessages + " ---");
            String result = messageSystem.sendMessage();
            System.out.println(result);
        }
        
        System.out.println("\n" + messageSystem.getTotalMessagesDisplay());
        System.out.println("\n✓ All messages processed!");
    }

}
