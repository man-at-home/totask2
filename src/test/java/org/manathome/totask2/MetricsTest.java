package org.manathome.totask2;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.manathome.totask2.util.AAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;

/** test metrics (as possible, at least no exceptions). */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class MetricsTest {
    
    private static final Logger LOG = LoggerFactory.getLogger(MetricsTest.class);
    
    @Autowired private DataDogReporterInitializer ddri;
    @Autowired private CounterService       counterService;
    @Autowired private MetricRegistry       metricRegistry;

    /** DataDogReporter. */
    @Test
    public void testRegisterReporterAndMetrics() {
        
        AAssert.checkNotNull(ddri);
        assertTrue(ddri.isInitialized);
        
        counterService.increment("TOTASK2.test.metrics.run");
        
        Timer timerMetric = metricRegistry.timer("TOTASK2.test.metrics.timed");        
        try (Timer.Context context = timerMetric.time()) {
            LOG.debug("test dropwizard timer");
        }        
    }
    
    
    /** metrics. */
    @Test
    public void testActuatorMetrics() {  
        LOG.debug("test spring actuator metrics");
        counterService.increment("TOTASK2.test.metrics.counter");
        counterService.decrement("TOTASK2.test.metrics.counter");
    }

    /** metrics. */
    @Test
    public void testDropwizardMetrics() {
        
        Timer timerMetric = metricRegistry.timer("TOTASK2.test.metrics.dropwizard.timer");
        
        try (Timer.Context context = timerMetric.time()) {
                LOG.debug("test dropwizard timer");
        }    
        assertTrue(timerMetric.getCount() > 0);
        
        Histogram hist = metricRegistry
                .histogram("TOTASK2.test.metrics.dropwizard.histogram");
        
        hist.update(33);
        Snapshot snap = hist.getSnapshot();
        assertTrue(snap.getMax() <= 33);
        assertTrue(snap.getMean() > 0);
        
        metricRegistry.counter("TOTASK2.test.metrics.dropwizard.counter").inc();
        
        assertTrue(metricRegistry.getCounters().size() > 0);
        assertTrue(metricRegistry.getTimers().size() > 0);
    }    
}
