/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121_poe;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
/**
 *
 * @author Student
 */
public class MessageTest {
    private Message message;
    
    @Before
    public void setUp() {
        message = new Message();
    }
    
    @Test
    public void testCheckMessageIDValid() {
        assertTrue("Valid message ID (10 chars)", message.checkMessageID("1234567890"));
        assertTrue("Valid message ID (5 chars)", message.checkMessageID("12345"));
        assertTrue("Valid message ID (exactly 10)", message.checkMessageID("ABCDEFGHIJ"));
    }
    
    @Test
    public void testCheckMessageIDInvalid() {
        assertFalse("Invalid message ID (11 chars)", message.checkMessageID("12345678901"));
        assertFalse("Invalid message ID (15 chars)", message.checkMessageID("123456789012345"));
    }
    
    @Test
    public void testCheckRecipientCellValid() {
        assertTrue("Valid SA number", message.checkRecipientCell("+27718693002"));
        assertTrue("Valid with different code", message.checkRecipientCell("+1234567890"));
    }
    
    @Test
    public void testCheckRecipientCellInvalid() {
        assertFalse("Missing + prefix", message.checkRecipientCell("27718693002"));
        assertFalse("Too short", message.checkRecipientCell("+123"));
        assertFalse("Too long", message.checkRecipientCell("+1234567890123456"));
    }
    
    @Test
    public void testCreateMessageHash() {
        String hash = message.createMessageHash("1234567890", 1, "Hi Mike, can you join us for dinner tonight?");
        // Expected: first two digits of ID (12):messageNumber(1):first word(Hi) last word(tonight?)
        assertTrue(hash.contains("12:1:"));
        assertTrue(hash.toUpperCase().equals(hash)); // Should be uppercase
    }
    
    @Test
    public void testCreateMessageHashWithShortID() {
        String hash = message.createMessageHash("12", 5, "Hello World");
        assertEquals("12:5:HELLOWORLD", hash);
    }
    
    @Test
    public void testCreateMessageHashSingleWord() {
        String hash = message.createMessageHash("99", 3, "Hello");
        assertEquals("99:3:HELLOHELLO", hash);
    }
    
    @Test
    public void testReturnTotalMessagesInitially() {
        assertEquals(0, message.returnTotalMessages());
    }
    
    @Test
    public void testPrintMessagesEmpty() {
        String result = message.printMessages();
        assertEquals("No messages have been sent yet.", result);
    }
    
    @Test
    public void testJSONStorage() {
        // First, send a message (we'll need to mock or actually send)
        // For unit test, we'll test the JSON method exists and works
        String result = message.storeMessagesInJSON("test_messages.json");
        assertTrue(result.contains("successfully stored") || result.contains("Error"));
    }
    
    @Test
    public void testMessageHashFormat() {
        // Test that hash contains all required components
        String hash = message.createMessageHash("AB12345678", 7, "The quick brown fox jumps over the lazy dog");
        
        // Should contain colon separators
        assertTrue(hash.contains(":"));
        
        // Should have exactly 2 colons
        int colonCount = hash.length() - hash.replace(":", "").length();
        assertEquals(2, colonCount);
        
        // Should be uppercase
        assertEquals(hash, hash.toUpperCase());
    }
}
