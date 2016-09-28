package org.exoplatform.addon.statistics.portlet;

import juzu.*;
import juzu.plugin.ajax.Ajax;
import juzu.template.Template;
import org.exoplatform.addon.statistics.services.HibernateStatisticsService;
import org.exoplatform.addon.statistics.util.StatisticsUtils;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.jmx.StatisticsService;
import org.hibernate.stat.*;
import org.hibernate.stat.internal.ConcurrentStatisticsImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by exo on 8/26/16.
 */

@SessionScoped
public class HibernateStatisticsController {
    private static final Log LOGGER = ExoLogger.getLogger(HibernateStatisticsController.class);
    @Inject
    HibernateStatisticsService hibernateStatisticsService;
    @Inject
    StatisticsService statisticsService;
    @Inject
    @Path("index.gtmpl")
    Template index;
    @Inject
    @Path("queries.gtmpl")
    Template queries;

    @Inject
    @Path("entities.gtmpl")
    Template entities;

    @Inject
    @Path("collections.gtmpl")
    Template collections;

    @Inject
    @Path("cache.gtmpl")
    Template cache;
 double maxQueryPerformance=0.0;

    @View
    public Response.Content index() throws IOException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        SessionFactoryImplementor hibernateStatistics = hibernateStatisticsService.generateStatistics();
        String [] queries = hibernateStatistics.getStatistics().getQueries();
        for (String query : queries) {

            QueryStatistics qstats = hibernateStatistics.getStatistics().getQueryStatistics(query);
            double maxQueryPerformance = Math.max(this.maxQueryPerformance, StatisticsUtils.toQueryPerformance(qstats));
            if (qstats.getCachePutCount() == 0 && qstats.getCacheHitCount() == 0)
                parameters.put("Cached","n/a");
            else
            {
                parameters.put("Cached",Math.round((StatisticsUtils.toRatio(qstats.getCacheHitCount(), qstats.getCacheMissCount())*100D)* 1000D) / 1000D);
            }
            parameters.put("Performance",Math.round(StatisticsUtils.performanceTableCell(maxQueryPerformance,StatisticsUtils.toQueryPerformance(qstats)))*100D);
            parameters.put("DBTime",Math.round(StatisticsUtils.toTotalAverageTime(qstats) / 1000D));
            parameters.put("Invocations", qstats.getExecutionCount() + qstats.getCacheHitCount());
            parameters.put("RowsFetched", qstats.getExecutionRowCount());
        }

        parameters.put("queries" , queries);
        loadCollectionsStatistics(parameters, hibernateStatistics);
        loadEntitiesStatistics(parameters, hibernateStatistics);
        return index.with(parameters).ok();


    }

    private void loadCollectionsStatistics(Map<String, Object> parameters, SessionFactoryImplementor hibernateStatistics) {
        String [] collectionsStatistics = hibernateStatistics.getStatistics().getCollectionRoleNames();
        for (String collection : collectionsStatistics) {
            CollectionStatistics collectionStatistics = hibernateStatistics.getStatistics().getCollectionStatistics(collection);
            Long cacheHits=hibernateStatistics.getStatistics().getSecondLevelCacheHitCount();
            if (collectionStatistics.getLoadCount() + cacheHits == 0)
                 parameters.put("CollectionPerformance","n/a");
            else if (collectionStatistics.getLoadCount() + collectionStatistics.getFetchCount() + cacheHits==0){
                 parameters.put("CollectionPerformance",0);
            }
            else{
                 parameters.put("CollectionPerformance",(Math.max(0, (collectionStatistics.getLoadCount() + cacheHits) - (collectionStatistics.getRemoveCount() + collectionStatistics.getRecreateCount() + collectionStatistics.getUpdateCount())))/collectionStatistics.getLoadCount() + collectionStatistics.getFetchCount() + cacheHits);
            }
                parameters.put("CollectionAccessCount", collectionStatistics.getFetchCount()+collectionStatistics.getLoadCount()+cacheHits);
                parameters.put("CollectionLoadsCount", collectionStatistics.getLoadCount());
                parameters.put("CollectionFetchesCount", collectionStatistics.getFetchCount());
                parameters.put("CollectionRecreationCount", collectionStatistics.getRecreateCount());
                parameters.put("CollectionModificationCount", collectionStatistics.getRecreateCount()+collectionStatistics.getUpdateCount()+collectionStatistics.getRemoveCount());
        }
                parameters.put("collectionsStatistics" , collectionsStatistics);
    }

   private void loadEntitiesStatistics(Map<String, Object> parameters, SessionFactoryImplementor hibernateStatistics) {
       String[] entitiesStatistics = hibernateStatistics.getStatistics().getEntityNames();
           for (String entity : entitiesStatistics) {
               EntityStatistics entityStatistics = hibernateStatistics.getStatistics().getEntityStatistics(entity);
               Long cacheHits=hibernateStatistics.getStatistics().getSecondLevelCacheHitCount();
               if (entityStatistics.getLoadCount()+cacheHits == 0)
                   parameters.put("EntityPerformance","n/a");
               else if (entityStatistics.getLoadCount() + entityStatistics.getFetchCount() + cacheHits==0){
                   parameters.put("EntityPerformance",0);
               }
               else{
                   parameters.put("EntityPerformance",(Math.max(0, (entityStatistics.getLoadCount() + cacheHits) - (entityStatistics.getDeleteCount() + entityStatistics.getInsertCount() + entityStatistics.getUpdateCount())))/entityStatistics.getLoadCount() + entityStatistics.getFetchCount() + cacheHits);
               }
               parameters.put("EntityAccessCount", entityStatistics.getFetchCount()+entityStatistics.getLoadCount()+cacheHits);
               parameters.put("EntityLoadsCount", entityStatistics.getLoadCount());
               parameters.put("EntityFetchesCount", entityStatistics.getFetchCount());
               parameters.put("EntityOptimisticFailureCount", entityStatistics.getOptimisticFailureCount());
               parameters.put("EntityModificationCount", entityStatistics.getInsertCount()+entityStatistics.getUpdateCount()+entityStatistics.getDeleteCount());
       }
       parameters.put("entitiesStatistics",entitiesStatistics);
   }
}
