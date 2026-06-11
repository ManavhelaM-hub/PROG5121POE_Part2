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
    
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        loginSystem = new Login();
        messageSystem = new Message();
        
        System.out.println("=".repeat(60));
        System.out.println("     WELCOME TO QUICKCHAT");
        System.out.println("=".repeat(60));
        System.out.println();
        
        // Load any existing messages
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
        scanner.nextLine();
        
        // Main application menu
        runMessagingApp();
        
        scanner.close();
        System.out.println("\nThank you for using QuickChat!");
    }
    
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
                System.out.println("❌ Username must contain '_' and be ≤ 5 characters");
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
                System.out.println("❌ Password must have 8+ chars, 1 capital, 1 number, 1 special char");
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
                System.out.println("❌ Must start with +27 followed by 9 digits");
            } else {
                phoneValid = true;
            }
        } while (!phoneValid);
        
        String registrationResult = loginSystem.registerUser(username, password, phoneNumber, firstName, lastName);
        System.out.println("\n" + registrationResult);
        
        return registrationResult.equals("User successfully registered!");
    }
    
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
    
    private static void runMessagingApp() {
        boolean running = true;
        
        while (running) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("     QUICKCHAT MAIN MENU");
            System.out.println("=".repeat(50));
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Stored Messages Management");
            System.out.println("4. Quit");
            System.out.print("\nChoose option (1-4): ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    sendMessages();
                    break;
                case 2:
                    displaySentMessages();
                    break;
                case 3:
                    storedMessagesMenu();
                    break;
                case 4:
                    System.out.println("\n" + messageSystem.storeMessagesInJSON("messages.json"));
                    System.out.println(messageSystem.getTotalMessagesDisplay());
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1, 2, 3, or 4.");
            }
        }
    }
    
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
    
    private static void displaySentMessages() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("     SENT MESSAGES");
        System.out.println("=".repeat(50));
        
        var sentMessages = messageSystem.getSentMessages();
        if (sentMessages.isEmpty()) {
            System.out.println("No messages have been sent yet.");
        } else {
            for (var msg : sentMessages) {
                System.out.println("\nMessage ID: " + msg.getMessageID());
                System.out.println("To: " + msg.getRecipient());
                System.out.println("Message: " + msg.getMessageText());
                System.out.println("Hash: " + msg.getMessageHash());
                System.out.println("-".repeat(40));
            }
        }
    }
    
    /**
     * PART 3: Stored Messages Management Menu
     */
    private static void storedMessagesMenu() {
        boolean backToMain = false;
        
        while (!backToMain) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("     STORED MESSAGES MANAGEMENT");
            System.out.println("=".repeat(50));
            System.out.println("1. Display sender and recipient of all stored messages");
            System.out.println("2. Display the longest stored message");
            System.out.println("3. Search for a message by ID");
            System.out.println("4. Search all messages for a particular recipient");
            System.out.println("5. Delete a message using message hash");
            System.out.println("6. Display full report of all stored messages");
            System.out.println("7. Back to Main Menu");
            System.out.print("\nChoose option (1-7): ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    System.out.println(messageSystem.displayStoredMessagesSenderRecipient());
                    break;
                case 2:
                    System.out.println("\n" + messageSystem.displayLongestStoredMessage());
                    break;
                case 3:
                    System.out.print("Enter Message ID to search: ");
                    String msgID = scanner.nextLine();
                    System.out.println("\n" + messageSystem.searchByMessageID(msgID));
                    break;
                case 4:
                    System.out.print("Enter recipient number (e.g., +27838884567): ");
                    String recipient = scanner.nextLine();
                    System.out.println("\n" + messageSystem.searchByRecipient(recipient));
                    break;
                case 5:
                    System.out.print("Enter Message Hash to delete: ");
                    String hash = scanner.nextLine();
                    System.out.println("\n" + messageSystem.deleteMessageByHash(hash));
                    break;
                case 6:
                    System.out.println(messageSystem.displayFullReport());
                    break;
                case 7:
                    backToMain = true;
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-7.");
            }
        }
    }
}
