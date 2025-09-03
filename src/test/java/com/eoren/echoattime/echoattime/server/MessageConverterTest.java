package com.eoren.echoattime.echoattime.server;

import com.eoren.echoattime.echoattime.redis.KeyGenerator;
import com.eoren.echoattime.echoattime.redis.pojo.TimedMessage;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageConverterTest {

  @Mock
  private KeyGenerator keyGenerator;
  
  private MessageConverter messageConverter;

  @Before
  public void setUp() {
    when(keyGenerator.generate()).thenReturn("test-key-123");
    messageConverter = new MessageConverter(keyGenerator);
  }

  @Test
  public void testValidMessageConversion() {
    String validMessage = "message:Hello World;time:5000";
    TimedMessage result = messageConverter.apply(validMessage);
    
    assertNotNull("Should convert valid message", result);
    assertEquals("Should extract correct message", "Hello World", result.getMessage());
    assertEquals("Should use generated key", "test-key-123", result.getId());
    assertTrue("Should set future time", result.getTimeInMillisToEcho() > System.currentTimeMillis());
  }

  @Test
  public void testInvalidMessageFormat() {
    String invalidMessage = "invalid format";
    TimedMessage result = messageConverter.apply(invalidMessage);
    assertNull("Should return null for invalid format", result);
  }

  @Test
  public void testNullMessage() {
    TimedMessage result = messageConverter.apply(null);
    assertNull("Should return null for null input", result);
  }

  @Test
  public void testEmptyMessage() {
    TimedMessage result = messageConverter.apply("");
    assertNull("Should return null for empty input", result);
  }

  @Test
  public void testNegativeTime() {
    String messageWithNegativeTime = "message:Test;time:-1000";
    TimedMessage result = messageConverter.apply(messageWithNegativeTime);
    assertNull("Should return null for negative time", result);
  }
}