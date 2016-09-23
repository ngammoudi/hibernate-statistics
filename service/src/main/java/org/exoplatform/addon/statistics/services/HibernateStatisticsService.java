package org.exoplatform.addon.statistics.services;


import org.exoplatform.commons.persistence.impl.EntityManagerService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.jmx.StatisticsService;
import org.hibernate.stat.Statistics;
import org.picocontainer.Startable;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * Created by exo on 9/8/16.
 */
public class HibernateStatisticsService implements Startable {

    private static final Log LOGGER = ExoLogger.getLogger(HibernateStatisticsService.class);
    private static final String HIBERNATE_STATISTICS_MBEAN = "Hibernate:application=Statistics";
    private MBeanServer mBeanServer ;
    private EntityManagerService entityManagerService;
    private SessionFactoryImplementor sessionFactory;
    StatisticsService statsMBean;

    public HibernateStatisticsService(EntityManagerService entityManageService)
    {

        this.entityManagerService =entityManageService;

    }
    public void start() {
        Session session = (Session) entityManagerService.createEntityManager().getDelegate();
        sessionFactory = (SessionFactoryImplementor) session.getSessionFactory();
        mBeanServer= ManagementFactory.getPlatformMBeanServer();
        try {

            if( !mBeanServer.isRegistered(new ObjectName(HIBERNATE_STATISTICS_MBEAN))){
                LOGGER.info("==== Start Register MBean Hibernate Statistics ==== ");
                statsMBean = new StatisticsService();
                statsMBean.setSessionFactory(sessionFactory);
                statsMBean.setStatisticsEnabled(true);
                mBeanServer.registerMBean(statsMBean, new ObjectName(HIBERNATE_STATISTICS_MBEAN));
                LOGGER.info("==== End Register MBean Hibernate Statistics ==== ");
            }

        } catch (Exception e) {
            LOGGER.error("Error during register MBean Hibernate Statistics");
        }
    }

    public void stop() {

    }
    public SessionFactoryImplementor generateStatistics( ){
       return sessionFactory;

    }

}
