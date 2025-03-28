package com.sprint.mission.discodeit.service.monitoring;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Service;

@Service
public class HibernateStatisticsService {

    @PersistenceContext
    private EntityManager em;

    public void logQueryStatistics() {
        SessionFactory sessionFactory = em.getEntityManagerFactory().unwrap(SessionFactory.class);
        Statistics stats = sessionFactory.getStatistics();
        stats.setStatisticsEnabled(true);

        System.out.println("총 실행된 SQL 쿼리 개수: " + stats.getQueryExecutionCount());
    }
}
