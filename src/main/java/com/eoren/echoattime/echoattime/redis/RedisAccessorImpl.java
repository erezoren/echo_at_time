package com.eoren.echoattime.echoattime.redis;

import com.eoren.echoattime.echoattime.redis.pojo.TimedMessage;
import com.eoren.echoattime.echoattime.redis.repository.TimedMessagesRepository;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Component;

@Component
public class RedisAccessorImpl implements RedisAccessor {

  private final TimedMessagesRepository timedMessagesRepository;

  public RedisAccessorImpl(TimedMessagesRepository timedMessagesRepository) {
    this.timedMessagesRepository = timedMessagesRepository;
  }

  @Override
  public void insertNewTimesMessage(TimedMessage timedMessage) {
    timedMessagesRepository.save(timedMessage);
  }

  @Override
  public void deleteTimesMessage(TimedMessage timedMessage) {
    timedMessagesRepository.delete(timedMessage);
  }

  @Override
  public List<TimedMessage> getAllStuckMessages() {
    Iterable<TimedMessage> all = timedMessagesRepository.findAll();
    if (isEmpty(all)) {
      return Collections.EMPTY_LIST;
    }
    return sortAndFilterByInsertionDateAndDelayTime(all);
  }

  private boolean isEmpty(Iterable<TimedMessage> all) {
    return all.spliterator().trySplit() == null;
  }

  private List<TimedMessage> sortAndFilterByInsertionDateAndDelayTime(Iterable<TimedMessage> all) {
    long now = new Date().getTime();
    List<TimedMessage> allAsList = StreamSupport
        .stream(all.spliterator(), false).filter(m -> m.getTimeInMillisToEcho() >= now)
        .collect(Collectors.toList());

    Collections.sort(allAsList, compareTitles());
    return allAsList;
  }

  private Comparator<TimedMessage> compareTitles() {
    return Comparator.comparing(TimedMessage::getTimeInMillisToEcho);
  }
}
