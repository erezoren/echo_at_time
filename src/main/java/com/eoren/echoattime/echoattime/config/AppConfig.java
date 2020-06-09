package com.eoren.echoattime.echoattime.config;

import com.eoren.echoattime.echoattime.Exception.AppServerException;
import com.eoren.echoattime.echoattime.redis.RedisAccessor;
import com.eoren.echoattime.echoattime.server.AppServer;
import com.eoren.echoattime.echoattime.server.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
public class AppConfig {

  @Bean
  JedisConnectionFactory jedisConnectionFactory() {
    return new JedisConnectionFactory();
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(jedisConnectionFactory());
    return template;
  }

  @Bean
  public AppServer appServer(@Value("${app.server.port}") int appServerPort, RedisAccessor redisAccessor) throws AppServerException {
    return new AppServer(appServerPort, redisAccessor, new MessageConverter());
  }
}
