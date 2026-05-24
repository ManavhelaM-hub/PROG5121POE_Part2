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

/**
 *
 * @author Student
 */
public class Message {
     private ArrayList<MessageObject> sentMessages;
    private ArrayList<MessageObject> storedMessages;
    private int messageCounter;
    private Scanner scanner;
    
    /**
     * Inner class to represent a single message
     */
    private class MessageObject {
        String messageID;
        String recipient;
        String messageText;
        String messageHash;
        int messageNumber;
        String status; // "sent", "stored", "disregarded"
        
        MessageObject(String messageID, String recipient, String messageText, 
                      String messageHash, int messageNumber, String status) {
            this.messageID = messageID;
            this.recipient = recipient;
            this.messageText = messageText;
            this.messageHash = messageHash;
            this.messageNumber = messageNumber;
            this.status = status;
        }
    }
    
    /**
     * Constructor for Message class
     */
    public Message() {
        this.sentMessages = new ArrayList<>();
        this.storedMessages = new ArrayList<>();
        this.messageCounter = 0;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Method to ensure message ID is not more than ten characters
     * @param messageID The message ID to validate
     * @return true if valid, false otherwise
     */
    public boolean checkMessageID(String messageID) {
        return messageID.length() <= 10;
    }
    
    /**
     * Method to check recipient cell number is no more than ten characters 
     * and starts with international code
     * @param recipient The recipient's cell number
     * @return true if valid, false otherwise
     */
    public boolean checkRecipientCell(String recipient) {
        // Check if starts with + and length is appropriate (international code + digits)
        // For simplicity, we'll check it starts with + and has between 10-15 characters
        return recipient.startsWith("+") && recipient.length() <= 15 && recipient.length() >= 10;
    }
    
    /**
     * Method to create message hash
     * Format: first two digits of messageID:messageNumber:firstwordLASTWORD
     * Displayed in all caps
     * @param messageID The message ID
     * @param messageNumber The message number
     * @param messageText The message text
     * @return Generated message hash
     */
    public String createMessageHash(String messageID, int messageNumber, String messageText) {
        // Get first two characters of message ID
        String firstTwoDigits = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;
        
        // Get first word and last word from message
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        
        // Create hash: 00:0:FIRSTWORDLASTWORD
        String hash = firstTwoDigits + ":" + messageNumber + ":" + firstWord + lastWord;
        
        return hash.toUpperCase();
    }
    
    /**
     * Method to generate unique message ID (10-digit number)
     * @return Generated unique message ID
     */
    private String generateMessageID() {
        Random random = new Random();
        long messageID = 1000000000L + (long)(random.nextDouble() * 9000000000L);
        String id = String.valueOf(messageID);
        return id.length() > 10 ? id.substring(0, 10) : id;
    }
    
    /**
     * Method to validate message length (max 250 characters)
     * @param message The message to validate
     * @return true if valid, false otherwise
     */
    private boolean validateMessageLength(String message) {
        return message.length() <= 250;
    }
    
    /**
     * Method to send a message (allows user to choose send, store, or disregard)
     * @return Status message
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
                sentMessages.add(new MessageObject(messageID, recipient, messageText, 
                                                   messageHash, messageCounter, status));
                break;
            case 2:
                status = "disregarded";
                resultMessage = "Press 0 to delete the message.";
                // Don't add to any list
                break;
            case 3:
                status = "stored";
                resultMessage = "Message successfully stored.";
                storedMessages.add(new MessageObject(messageID, recipient, messageText, 
                                                     messageHash, messageCounter, status));
                break;
            default:
                status = "unknown";
                resultMessage = "Invalid option. Message not processed.";
        }
        
        // Display full message details if sent or stored
        if (status.equals("sent") || status.equals("stored")) {
            displayMessageDetails(messageID, messageHash, recipient, messageText);
        }
        
        return resultMessage;
    }
    
    /**
     * Method to display full message details
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
     * Method to return all sent messages
     * @return Formatted string of all sent messages
     */
    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent yet.";
        }
        
        StringBuilder output = new StringBuilder();
        output.append("\n=== SENT MESSAGES ===\n");
        for (MessageObject msg : sentMessages) {
            output.append("Message #").append(msg.messageNumber).append("\n");
            output.append("ID: ").append(msg.messageID).append("\n");
            output.append("Hash: ").append(msg.messageHash).append("\n");
            output.append("To: ").append(msg.recipient).append("\n");
            output.append("Message: ").append(msg.messageText).append("\n");
            output.append("Status: ").append(msg.status).append("\n");
            output.append("------------------------\n");
        }
        return output.toString();
    }
    
    /**
     * Method to return total number of messages sent
     * @return Total count of sent messages
     */
    public int returnTotalMessages() {
        return sentMessages.size();
    }
    
    /**
     * Method to store messages in JSON file
     * Reference: JSON file storage using org.json library
     * @param filename Name of the JSON file to store messages
     * @return Success or error message
     */
     public String storeMessagesInJSON(String filename) throws JSONException {
        try {
            JSONArray jsonArray = new JSONArray();
            
            // Add sent messages
            for (MessageObject msg : sentMessages) {
                JSONObject jsonMsg = new JSONObject();
                jsonMsg.put("messageID", msg.messageID);
                jsonMsg.put("messageNumber", msg.messageNumber);
                jsonMsg.put("recipient", msg.recipient);
                jsonMsg.put("messageText", msg.messageText);
                jsonMsg.put("messageHash", msg.messageHash);
                jsonMsg.put("status", msg.status);
                jsonMsg.put("type", "sent");
                jsonArray.put(jsonMsg);
            }
            
            // Add stored messages
            for (MessageObject msg : storedMessages) {
                JSONObject jsonMsg = new JSONObject();
                jsonMsg.put("messageID", msg.messageID);
                jsonMsg.put("messageNumber", msg.messageNumber);
                jsonMsg.put("recipient", msg.recipient);
                jsonMsg.put("messageText", msg.messageText);
                jsonMsg.put("messageHash", msg.messageHash);
                jsonMsg.put("status", msg.status);
                jsonMsg.put("type", "stored");
                jsonArray.put(jsonMsg);
            }
            
            // Write to file
            try (FileWriter file = new FileWriter(filename)) {
                file.write(jsonArray.toString(4)); // Indent with 4 spaces
                file.flush();
            }
            
            return "Messages successfully stored in " + filename;
            
        } catch (IOException e) {
            return "Error storing messages: " + e.getMessage();
        }
    }
    
    /**
     * Method to load messages from JSON file
     * @param filename Name of the JSON file to load
     * @return Success or error message
     */
    public String loadMessagesFromJSON(String filename) throws JSONException {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            JSONArray jsonArray = new JSONArray(content);
            
            sentMessages.clear();
            storedMessages.clear();
            messageCounter = 0;
            
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonMsg = jsonArray.getJSONObject(i);
                MessageObject msg = new MessageObject(
                    jsonMsg.getString("messageID"),
                    jsonMsg.getString("recipient"),
                    jsonMsg.getString("messageText"),
                    jsonMsg.getString("messageHash"),
                    jsonMsg.getInt("messageNumber"),
                    jsonMsg.getString("status")
                );
                
                if (jsonMsg.getString("type").equals("sent")) {
                    sentMessages.add(msg);
                } else {
                    storedMessages.add(msg);
                }
                
                if (msg.messageNumber > messageCounter) {
                    messageCounter = msg.messageNumber;
                }
            }
            
            return "Messages successfully loaded from " + filename;
            
        } catch (IOException e) {
            return "No existing messages file found. Starting fresh.";
        }
    }
    
    /**
     * Method to get number of sent messages for display
     * @return Formatted string with total count
     */
    public String getTotalMessagesDisplay() {
        int total = returnTotalMessages();
        return "Total number of messages sent: " + total;
    }

}
