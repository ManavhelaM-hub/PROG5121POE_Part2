/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121_poe;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONException;

/*part 3*/
import java.io.*;
import java.nio.file.*;

/**
 *
 * @author Student
 */
public class Message {
    // Arrays for storing different message types
    private ArrayList<MessageObject> sentMessages;
    private ArrayList<MessageObject> disregardedMessages;
    private ArrayList<MessageObject> storedMessages;
    private ArrayList<String> messageHashes;
    private ArrayList<String> messageIDs;
    
    private int messageCounter;
    private Scanner scanner;
    
    /**
     * Public MessageObject class to represent a single message
     * Made public so other classes can access it
     */
    public static class MessageObject {
        private String messageID;
        private String recipient;
        private String messageText;
        private String messageHash;
        private int messageNumber;
        private String status;
        
        public MessageObject(String messageID, String recipient, String messageText, 
                              String messageHash, int messageNumber, String status) {
            this.messageID = messageID;
            this.recipient = recipient;
            this.messageText = messageText;
            this.messageHash = messageHash;
            this.messageNumber = messageNumber;
            this.status = status;
        }
        
        // Getters
        public String getMessageID() { return messageID; }
        public String getRecipient() { return recipient; }
        public String getMessageText() { return messageText; }
        public String getMessageHash() { return messageHash; }
        public int getMessageNumber() { return messageNumber; }
        public String getStatus() { return status; }
        
        @Override
        public String toString() {
            return messageNumber + "|" + messageID + "|" + recipient + "|" + 
                   messageText + "|" + messageHash + "|" + status;
        }
        
        static MessageObject fromString(String line) {
            String[] parts = line.split("\\|");
            if (parts.length == 6) {
                MessageObject msg = new MessageObject(
                    parts[1], parts[2], parts[3], parts[4], 
                    Integer.parseInt(parts[0]), parts[5]
                );
                return msg;
            }
            return null;
        }
    }
    
    /**
     * Constructor for Message class - initializes all arrays
     */
    public Message() {
        this.sentMessages = new ArrayList<>();
        this.disregardedMessages = new ArrayList<>();
        this.storedMessages = new ArrayList<>();
        this.messageHashes = new ArrayList<>();
        this.messageIDs = new ArrayList<>();
        this.messageCounter = 0;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Method to ensure message ID is not more than ten characters
     */
    public boolean checkMessageID(String messageID) {
        return messageID.length() <= 10;
    }
    
    /**
     * Method to check recipient cell number
     */
    public boolean checkRecipientCell(String recipient) {
        return recipient.startsWith("+") && recipient.length() <= 15 && recipient.length() >= 10;
    }
    
    /**
     * Method to create message hash
     * Format: first two digits of messageID:messageNumber:firstwordLASTWORD
     */
    public String createMessageHash(String messageID, int messageNumber, String messageText) {
        String firstTwoDigits = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        String hash = firstTwoDigits + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }
    
    /**
     * Generate unique message ID (10-digit number)
     */
    private String generateMessageID() {
        Random random = new Random();
        long messageID = 1000000000L + (long)(random.nextDouble() * 9000000000L);
        String id = String.valueOf(messageID);
        String finalId = id.length() > 10 ? id.substring(0, 10) : id;
        
        // Ensure unique ID
        while (messageIDs.contains(finalId)) {
            messageID = 1000000000L + (long)(random.nextDouble() * 9000000000L);
            id = String.valueOf(messageID);
            finalId = id.length() > 10 ? id.substring(0, 10) : id;
        }
        
        return finalId;
    }
    
    /**
     * Validate message length (max 250 characters)
     */
    private boolean validateMessageLength(String message) {
        return message.length() <= 250;
    }
    
    /**
     * Add message to appropriate array based on status
     */
    private void addToArray(MessageObject msg) {
        // Add to specific message array
        switch (msg.getStatus()) {
            case "sent":
                sentMessages.add(msg);
                break;
            case "stored":
                storedMessages.add(msg);
                break;
            case "disregarded":
                disregardedMessages.add(msg);
                break;
        }
        
        // Always add to hash and ID arrays for tracking
        messageHashes.add(msg.getMessageHash());
        messageIDs.add(msg.getMessageID());
    }
    
    /**
     * Method to send a message (allows user to choose send, store, or disregard)
     */
    public String sendMessage() {
        System.out.println("\n--- Compose New Message ---");
        
        // Get recipient
        String recipient;
        boolean validRecipient = false;
        do {
            System.out.print("Enter recipient cell number (format: +277XXXXXXXX): ");
            recipient = scanner.nextLine();
            if (!checkRecipientCell(recipient)) {
                System.out.println("Invalid recipient number. Must start with + and be 10-15 characters.");
            } else {
                validRecipient = true;
            }
        } while (!validRecipient);
        
        // Get message
        String messageText;
        boolean validMessage = false;
        do {
            System.out.print("Enter your message (max 250 characters): ");
            messageText = scanner.nextLine();
            if (!validateMessageLength(messageText)) {
                System.out.println("Please enter a message of less than 250 characters.");
            } else {
                validMessage = true;
            }
        } while (!validMessage);
        
        // Generate message ID and increment counter
        String messageID = generateMessageID();
        messageCounter++;
        
        // Create message hash
        String messageHash = createMessageHash(messageID, messageCounter, messageText);
        
        System.out.println("\nMessage ID generated: " + messageID);
        System.out.println("Message Hash generated: " + messageHash);
        
        // Ask user what to do with the message
        System.out.println("\nWhat would you like to do with this message?");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message");
        System.out.print("Choose option (1-3): ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        String status;
        String resultMessage;
        
        switch (choice) {
            case 1:
                status = "sent";
                resultMessage = "Message successfully sent.";
                break;
            case 2:
                status = "disregarded";
                resultMessage = "Press 0 to delete the message.";
                break;
            case 3:
                status = "stored";
                resultMessage = "Message successfully stored.";
                break;
            default:
                status = "unknown";
                resultMessage = "Invalid option. Message not processed.";
        }
        
        if (!status.equals("unknown")) {
            MessageObject msg = new MessageObject(messageID, recipient, messageText, 
                                                   messageHash, messageCounter, status);
            addToArray(msg);
            
            // Display full message details if sent or stored
            if (status.equals("sent") || status.equals("stored")) {
                displayMessageDetails(messageID, messageHash, recipient, messageText);
            }
        }
        
        return resultMessage;
    }
    
    /**
     * Display full message details
     */
    private void displayMessageDetails(String messageID, String messageHash, 
                                       String recipient, String messageText) {
        System.out.println("\n--- Message Details ---");
        System.out.println("Message ID: " + messageID);
        System.out.println("Message Hash: " + messageHash);
        System.out.println("Recipient: " + recipient);
        System.out.println("Message: " + messageText);
        System.out.println("------------------------\n");
    }
    
    /**
     * Get sent messages array
     */
    public ArrayList<MessageObject> getSentMessages() {
        return sentMessages;
    }
    
    /**
     * Get stored messages array
     */
    public ArrayList<MessageObject> getStoredMessages() {
        return storedMessages;
    }
    
    /**
     * Get disregarded messages array
     */
    public ArrayList<MessageObject> getDisregardedMessages() {
        return disregardedMessages;
    }
    
    /**
     * Get message hashes array
     */
    public ArrayList<String> getMessageHashes() {
        return messageHashes;
    }
    
    /**
     * Get message IDs array
     */
    public ArrayList<String> getMessageIDs() {
        return messageIDs;
    }
    
    /**
     * PART 3: Display sender and recipient of all stored messages
     */
    public String displayStoredMessagesSenderRecipient() {
        if (storedMessages.isEmpty()) {
            return "No stored messages available.";
        }
        
        StringBuilder output = new StringBuilder();
        output.append("\n=== STORED MESSAGES - SENDER & RECIPIENT ===\n");
        for (MessageObject msg : storedMessages) {
            output.append("Sender: User | Recipient: ").append(msg.getRecipient()).append("\n");
        }
        return output.toString();
    }
    
    /**
     * PART 3: Display the longest stored message
     */
    public String displayLongestStoredMessage() {
        if (storedMessages.isEmpty()) {
            return "No stored messages available.";
        }
        
        MessageObject longest = storedMessages.get(0);
        for (MessageObject msg : storedMessages) {
            if (msg.getMessageText().length() > longest.getMessageText().length()) {
                longest = msg;
            }
        }
        
        return "Longest stored message: \"" + longest.getMessageText() + "\"";
    }
    
    /**
     * PART 3: Search for a message by ID and display recipient and message
     */
    public String searchByMessageID(String messageID) {
        for (MessageObject msg : storedMessages) {
            if (msg.getMessageID().equals(messageID)) {
                return "Recipient: " + msg.getRecipient() + "\nMessage: " + msg.getMessageText();
            }
        }
        return "Message ID not found in stored messages.";
    }
    
    /**
     * PART 3: Search all messages for a particular recipient
     */
    public String searchByRecipient(String recipient) {
        StringBuilder results = new StringBuilder();
        boolean found = false;
        
        // Search in sent messages
        for (MessageObject msg : sentMessages) {
            if (msg.getRecipient().equals(recipient)) {
                results.append("[SENT] ").append(msg.getMessageText()).append("\n");
                found = true;
            }
        }
        
        // Search in stored messages
        for (MessageObject msg : storedMessages) {
            if (msg.getRecipient().equals(recipient)) {
                results.append("[STORED] ").append(msg.getMessageText()).append("\n");
                found = true;
            }
        }
        
        return found ? results.toString() : "No messages found for recipient: " + recipient;
    }
    
    /**
     * PART 3: Delete a message using message hash
     */
    public String deleteMessageByHash(String messageHash) {
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).getMessageHash().equals(messageHash)) {
                String deletedMessage = storedMessages.get(i).getMessageText();
                storedMessages.remove(i);
                messageHashes.remove(messageHash);
                return "Message: \"" + deletedMessage + "\" successfully deleted.";
            }
        }
        return "Message hash not found in stored messages.";
    }
    
    /**
     * PART 3: Display full report of all stored messages
     */
    public String displayFullReport() {
        if (storedMessages.isEmpty()) {
            return "No stored messages available to display.";
        }
        
        StringBuilder report = new StringBuilder();
        report.append("\n").append("=".repeat(60));
        report.append("\n           STORED MESSAGES FULL REPORT");
        report.append("\n").append("=".repeat(60));
        
        for (MessageObject msg : storedMessages) {
            report.append("\n\nMessage #").append(msg.getMessageNumber());
            report.append("\n├─ Message ID: ").append(msg.getMessageID());
            report.append("\n├─ Message Hash: ").append(msg.getMessageHash());
            report.append("\n├─ Recipient: ").append(msg.getRecipient());
            report.append("\n├─ Message: ").append(msg.getMessageText());
            report.append("\n└─ Status: ").append(msg.getStatus());
            report.append("\n").append("-".repeat(40));
        }
        
        report.append("\n\nTotal Stored Messages: ").append(storedMessages.size());
        report.append("\n").append("=".repeat(60));
        
        return report.toString();
    }
    
    /**
     * Populate arrays with test data (for testing purposes)
     */
    public void populateTestData() {
        // Test Data Message 1 - Sent
        String msg1ID = generateMessageID();
        MessageObject msg1 = new MessageObject(
            msg1ID, "+27834557896", "Did you get the cake?",
            createMessageHash(msg1ID, messageCounter + 1, "Did you get the cake?"),
            ++messageCounter, "sent"
        );
        addToArray(msg1);
        
        // Test Data Message 2 - Stored
        String msg2ID = generateMessageID();
        MessageObject msg2 = new MessageObject(
            msg2ID, "+27838884567", "Where are you? You are late! I have asked you to be on time.",
            createMessageHash(msg2ID, messageCounter + 1, "Where are you? You are late! I have asked you to be on time."),
            ++messageCounter, "stored"
        );
        addToArray(msg2);
        
        // Test Data Message 3 - Disregarded
        String msg3ID = generateMessageID();
        MessageObject msg3 = new MessageObject(
            msg3ID, "+27834484567", "Yohoooo, I am at your gate.",
            createMessageHash(msg3ID, messageCounter + 1, "Yohoooo, I am at your gate."),
            ++messageCounter, "disregarded"
        );
        addToArray(msg3);
        
        // Test Data Message 4 - Sent
        String msg4ID = generateMessageID();
        MessageObject msg4 = new MessageObject(
            msg4ID, "+27838884567", "It is dinner time !",
            createMessageHash(msg4ID, messageCounter + 1, "It is dinner time !"),
            ++messageCounter, "sent"
        );
        addToArray(msg4);
        
        // Test Data Message 5 - Stored
        String msg5ID = generateMessageID();
        MessageObject msg5 = new MessageObject(
            msg5ID, "+27838884567", "Ok, I am leaving without you.",
            createMessageHash(msg5ID, messageCounter + 1, "Ok, I am leaving without you."),
            ++messageCounter, "stored"
        );
        addToArray(msg5);
    }
    
    /**
     * Return total number of messages sent
     */
    public int returnTotalMessages() {
        return sentMessages.size();
    }
    
    /**
     * Return formatted total messages display
     */
    public String getTotalMessagesDisplay() {
        return "Total number of messages sent: " + sentMessages.size();
    }
    
    /**
     * Store messages in text file
     */
    public String storeMessagesInJSON(String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("# QuickChat Messages Export");
            writer.println("# Format: MessageNumber|MessageID|Recipient|MessageText|MessageHash|Status");
            
            writer.println("\n# Sent Messages:");
            for (MessageObject msg : sentMessages) {
                writer.println(msg.toString());
            }
            
            writer.println("\n# Stored Messages:");
            for (MessageObject msg : storedMessages) {
                writer.println(msg.toString());
            }
            
            writer.println("\n# Disregarded Messages:");
            for (MessageObject msg : disregardedMessages) {
                writer.println(msg.toString());
            }
            
            return "Messages successfully stored in " + filename;
            
        } catch (IOException e) {
            return "Error storing messages: " + e.getMessage();
        }
    }
    
    /**
     * Load messages from text file
     */
    public String loadMessagesFromJSON(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return "No existing messages file found. Starting fresh.";
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int maxNumber = 0;
            
            // Clear existing arrays
            sentMessages.clear();
            storedMessages.clear();
            disregardedMessages.clear();
            messageHashes.clear();
            messageIDs.clear();
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) {
                    continue;
                }
                
                MessageObject msg = MessageObject.fromString(line);
                if (msg != null) {
                    addToArray(msg);
                    if (msg.getMessageNumber() > maxNumber) {
                        maxNumber = msg.getMessageNumber();
                    }
                }
            }
            
            messageCounter = maxNumber;
            return "Messages successfully loaded from " + filename;
            
        } catch (IOException e) {
            return "Error loading messages: " + e.getMessage();
        }
    }
}
