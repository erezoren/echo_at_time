package com.eoren.echoattime.echoattime.redis.repository;

import com.eoren.echoattime.echoattime.redis.pojo.TimedMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimedMessagesRepository extends CrudRepository<TimedMessage, String> {

}

