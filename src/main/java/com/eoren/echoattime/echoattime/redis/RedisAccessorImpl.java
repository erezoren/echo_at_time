package com.eoren.echoattime.echoattime.redis;

import com.eoren.echoattime.echoattime.redis.pojo.TimedMessage;
import com.eoren.echoattime.echoattime.redis.repository.TimedMessagesRepository;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class RedisAccessorImpl implements RedisAccessor {

  private static final String OBEJCT_PREFIX = "TimesMessages:";
  private final TimedMessagesRepository timedMessagesRepository;
  private final Jedis jedis;

  public RedisAccessorImpl(TimedMessagesRepository timedMessagesRepository, Jedis jedis) {
    this.timedMessagesRepository = timedMessagesRepository;
    this.jedis = jedis;
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

  /*
  This is an optimization that uses Redis KEYS by pattern feature in order to fetch only messages to be displayed today
   */
  @Override
  public List<TimedMessage> getAllMessageByDatePattern(String datePattern) {
    Set<String> todaysKeys = jedis.keys(datePattern);
    List<TimedMessage> all = todaysKeys.stream().map(key -> key.replace(OBEJCT_PREFIX, ""))
        .map(key -> timedMessagesRepository.findById(key)).filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
    return sortAndFilterByInsertionDateAndDelayTime(all);
  }

  /*
  Iterable has a size of 1 even when it is empty
   */
  private boolean isEmpty(Iterable<TimedMessage> all) {
    return !all.iterator().hasNext();
  }

  private List<TimedMessage> sortAndFilterByInsertionDateAndDelayTime(Iterable<TimedMessage> all) {
    long now = new Date().getTime();
    List<TimedMessage> allAsList = StreamSupport
        .stream(all.spliterator(), false).filter(m -> m.getTimeInMillisToEcho() <= now)
        .collect(Collectors.toList());

    Collections.sort(allAsList, timeOfDisplayComparator());
    return allAsList;
  }

  private Comparator<TimedMessage> timeOfDisplayComparator() {
    return Comparator.comparing(TimedMessage::getTimeInMillisToEcho);
  }
}
