package com.sprint.mission.discodeit.service.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtils implements ApplicationContextAware {

  private static ApplicationContext ac;


  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    SpringContextUtils.ac = ac;
  }

  public static <T> T getBean(Class<T> beanClass) {
    if (ac == null) {
      throw new IllegalStateException("ApplicationContext가 초기화되지 않았습니다");
    }
    return ac.getBean(beanClass);
  }
}
