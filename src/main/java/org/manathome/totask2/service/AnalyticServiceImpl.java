package org.manathome.totask2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import io.keen.client.java.KeenClient;


/**
 * analytic data sink..
 * 
 * Demonstration only. Typically you have to decide: log it and/or send it here instead?
 * 
 * following to environment properties have to be set with keys from key.io website, to get data into keen.io:
 * <ul>
 *   <li>env.getProperty("KEEN_PROJECT_ID")</li>
 *   <li>env.getProperty("KEEN_WRITE_KEY")</li>
 * </ul>
 * 
 * @see    <a href="https://totask2.wordpress.com/2015/03/28/feeding-analytical-data-into-keen-io/">blog post</a>
 * @see    <a href="http://keen.io">keen.io</a>
 * 
 * @see    KeenClient
 * 
 * @author man-at-home
 * @since  2015-03-28
 */
@Service("AnalyticService")
public class AnalyticServiceImpl implements AnalyticService {
    
    private static final Logger LOG = LoggerFactory.getLogger(AnalyticServiceImpl.class); 

    /** keen.io client library. */
    @Autowired private KeenClient keenClient;   
    
    /** log analytical data for further processing. */
    public void logData(final String category, final String key, final String data) { 
        
        if (keenClient != null) {
            Map<String, Object> event = new HashMap<String, Object>();
            event.put(key, data);
            keenClient.addEventAsync(category, event);
            
            LOG.debug("addEventAsync to keen.io: ({0}/{1}:{2})", category, key, data);
        } else {
            LOG.error("null/no addEventAsync to keen.io: ({0}/{1}:{2})", category, key, data);            
        }            
    }

    /** log analytical data for further processing. */
    public void logMasterDataChanges(final String entityName, final long entityId, final String instanceName) { 
        
        if (keenClient != null) {
            Map<String, Object> event = new HashMap<String, Object>();
            event.put("entity", entityName);
            event.put("id", new Long(entityId));
            event.put("name", instanceName);
            keenClient.addEventAsync("masterData", event);
            
            LOG.debug("addEventAsync to keen.io:  {} {} {} ", entityName, entityId, instanceName);
        } else {
            LOG.warn("null/no addEventAsync to keen.io: {} {} {} ", entityName, entityId, instanceName);            
        }            
    }    
}

