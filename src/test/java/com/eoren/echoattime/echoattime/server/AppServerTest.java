package com.eoren.echoattime.echoattime.server;

import com.eoren.echoattime.echoattime.Exception.AppServerException;
import com.eoren.echoattime.echoattime.redis.RedisAccessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class AppServerTest {

  private int port = 1;
  @Mock
  private RedisAccessor redisAccessor;
  @Mock
  private MessageConverter messageConverter;
  private AppServer appServer;

  @Before
  public void setUp() throws AppServerException {
  }

  @Test
  public void test() {
  }
}