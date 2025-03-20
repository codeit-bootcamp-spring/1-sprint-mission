package com.sprint.mission.discodeit.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class IgnoreLogFilter extends Filter<ILoggingEvent> {

  @Override
  public FilterReply decide(ILoggingEvent event) {
    if (event.getMessage().contains("INSERT") || event.getMessage().length() == 0) {
      return FilterReply.DENY;
    } else {
      return FilterReply.ACCEPT;
    }
  }
}
