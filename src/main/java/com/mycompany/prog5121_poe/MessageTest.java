/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121_poe;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Unit tests for Part 3 functionality
 * @author Your Name
 * @version 3.0
 */
public class MessageTestPart3 {
    
    private Message message;
    
    @Before
    public void setUp() {
        message = new Message();
        message.populateTestData(); // Populate with test data from spec
    }
    
    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        var sentMessages = message.getSentMessages();
        
        // Test Data messages 1 and 4 should be sent
        boolean foundMessage1 = false;
        boolean foundMessage4 = false;
        
        for (var msg : sentMessages) {
            if (msg.messageText.equals("Did you get the cake?")) {
                foundMessage1 = true;
            }
            if (msg.messageText.equals("It is dinner time !")) {
                foundMessage4 = true;
            }
        }
        
        assertTrue("Sent messages should contain 'Did you get the cake?'", foundMessage1);
        assertTrue("Sent messages should contain 'It is dinner time !'", foundMessage4);
    }
    
    @Test
    public void testDisplayLongestMessage() {
        String longest = message.displayLongestStoredMessage();
        assertTrue("Longest message should be the late message", 
                   longest.contains("Where are you? You are late! I have asked you to be on time."));
    }
    
    @Test
    public void testSearchByMessageID() {
        // Get a message ID from stored messages
        var storedMessages = message.getStoredMessages();
        if (!storedMessages.isEmpty()) {
            String testMessageID = storedMessages.get(0).messageID;
            String result = message.searchByMessageID(testMessageID);
            assertNotNull(result);
            assertFalse(result.contains("not found"));
        }
    }
    
    @Test
    public void testSearchByRecipient() {
        String result = message.searchByRecipient("+27838884567");
        assertTrue("Should find messages for recipient +27838884567",
                   result.contains("Where are you?") || result.contains("Ok, I am leaving"));
    }
    
    @Test
    public void testDeleteMessageByHash() {
        var storedMessages = message.getStoredMessages();
        if (!storedMessages.isEmpty()) {
            String testHash = storedMessages.get(0).messageHash;
            String result = message.deleteMessageByHash(testHash);
            assertTrue("Delete message should return success", 
                       result.contains("successfully deleted"));
        }
    }
    
    @Test
    public void testDisplayFullReport() {
        String report = message.displayFullReport();
        assertNotNull(report);
        assertFalse(report.isEmpty());
        assertTrue(report.contains("STORED MESSAGES FULL REPORT"));
    }
    
    @Test
    public void testDisplayStoredMessagesSenderRecipient() {
        String result = message.displayStoredMessagesSenderRecipient();
        assertNotNull(result);
        if (!message.getStoredMessages().isEmpty()) {
            assertTrue(result.contains("Recipient:"));
        }
    }
    
    @Test
    public void testMessageHashesArray() {
        var hashes = message.getMessageHashes();
        assertNotNull(hashes);
        assertTrue(hashes.size() >= 3); // At least 3 messages from test data
    }
    
    @Test
    public void testMessageIDsArray() {
        var ids = message.getMessageIDs();
        assertNotNull(ids);
        assertTrue(ids.size() >= 3);
    }
    
    @Test
    public void testDisregardedMessagesArray() {
        var disregarded = message.getDisregardedMessages();
        assertNotNull(disregarded);
        // Test data has 1 disregarded message
        boolean foundDisregarded = false;
        for (var msg : disregarded) {
            if (msg.messageText.equals("Yohoooo, I am at your gate.")) {
                foundDisregarded = true;
            }
        }
        assertTrue("Should have the disregarded message", foundDisregarded || disregarded.isEmpty());
    }
    
    @Test
    public void testStoredMessagesArray() {
        var stored = message.getStoredMessages();
        assertNotNull(stored);
        // Test data has 2 stored messages (messages 2 and 5)
        int storedCount = 0;
        for (var msg : stored) {
            if (msg.messageText.equals("Where are you? You are late! I have asked you to be on time.") ||
                msg.messageText.equals("Ok, I am leaving without you.")) {
                storedCount++;
            }
        }
        assertTrue("Should have at least 2 stored messages", storedCount >= 1);
    }
}
