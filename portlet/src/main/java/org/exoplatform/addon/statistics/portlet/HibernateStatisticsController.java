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
        //loadEntitiesStatistics(parameters, hibernateStatistics);
        return index.with(parameters).ok();


    }

    private void loadCollectionsStatistics(Map<String, Object> parameters, SessionFactoryImplementor hibernateStatistics) {
        String [] collectionsStatistics = hibernateStatistics.getStatistics().getCollectionRoleNames();
        for (String collection : collectionsStatistics) {

            CollectionStatistics collectionStatistics = hibernateStatistics.getStatistics().getCollectionStatistics(collection);
if (collectionStatistics.getLoadCount() + hibernateStatistics.getStatistics().getSecondLevelCacheHitCount() == 0)
    parameters.put("CollectionPerformance","n/a");
            else if (collectionStatistics.getLoadCount() + collectionStatistics.getFetchCount() + hibernateStatistics.getStatistics().getSecondLevelCacheHitCount()==0){
    parameters.put("CollectionPerformance",0);
            }
            else{
    parameters.put("CollectionPerformance",(Math.max(0, (collectionStatistics.getLoadCount() + hibernateStatistics.getStatistics().getSecondLevelCacheHitCount()) - (collectionStatistics.getRemoveCount() + collectionStatistics.getRecreateCount() + collectionStatistics.getUpdateCount())))/collectionStatistics.getLoadCount() + collectionStatistics.getFetchCount() + hibernateStatistics.getStatistics().getSecondLevelCacheHitCount());
            }
           // parameters.put("AccessCount", new EntityPerformanceTableCell(collectionStatistics, hibernateStatistics.getStatistics().getQueryCacheHitCount()));
            parameters.put("AccessCount", collectionStatistics.getFetchCount()+collectionStatistics.getLoadCount());
            parameters.put("LoadsCount", collectionStatistics.getLoadCount());
            parameters.put("FetchesCount", collectionStatistics.getFetchCount());
            parameters.put("RecreationCount", collectionStatistics.getRecreateCount());
            parameters.put("ModificationCount", collectionStatistics.getRecreateCount() + collectionStatistics.getRemoveCount() + collectionStatistics.getUpdateCount());
        }
        parameters.put("collectionsStatistics" , collectionsStatistics);
    }

@Ajax
@Resource
    public Response.Content loadEntitiesStatistics() throws JSONException {
    JSONObject jsonObject = new JSONObject();

    SessionFactoryImplementor hibernateStatistics = hibernateStatisticsService.generateStatistics();
    String [] entitiesStatistics = hibernateStatistics.getStatistics().getEntityNames();

     try {
         for (String entity : entitiesStatistics) {
             EntityStatistics entityStatistics= hibernateStatistics.getStatistics().getEntityStatistics(entity);

             jsonObject.put("entityStatistics",entityStatistics);

         }

         }
     catch (JSONException e) {
        e.printStackTrace();
    }
    return Response.ok(jsonObject.toString()).withMimeType("application/json; charset=UTF-8").withHeader("Cache-Control", "no-cache");

    }

}
