package org.manathome.totask2;

import org.coursera.metrics.datadog.DatadogReporter;
import org.coursera.metrics.datadog.DatadogReporter.Expansion;
import org.coursera.metrics.datadog.transport.UdpTransport;
import org.manathome.totask2.util.AAssert;
import org.manathome.totask2.util.Totask2Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.Timer;

/*
* @startuml doc-files/totask2.metrics.datadog.png
*
* [Metric]          ..> [MetricRegistry]   
* [MetricRegistry]  ..> [DataDogReporter]   
* [DataDogReporter] ..> UDP : use
* UDP               ..> [local statsd agent DataDog]
* [DataDog Agent]   ..> HTTPS : use
* HTTPS             ..> [http://datadoghq.com]
*
* note right of [DataDogReporter]
*   sending metrics with statsd protocol 
* end note
* 
* @enduml
* 
*/    


/** 
 * configure metrics forwarding dropWizard metrics -> local dataDog "statsd" agent. 
 * 
 * on startup adding an reporter to dropWizard metrics library. Data flows via local agent to 
 * datadog metrics monitoring into the cloud.
 * 
 * uses following environment variables if present:
 * <ul>
 *    <li>OPENSHIFT_DATADOG_DOGSTATSD_PORT</li>
 *    <li>OPENSHIFT_DATADOG_IP</li>
 * </ul>
 * 
 * <img alt="package-uml" src="doc-files/totask2.metrics.datadog.png"/>
 * 
 * @see    MetricRegistry  metric registry containing sent metrics
 * @see    DatadogReporter forwarding metric reporter
 * @see    <a href="http://www.datadoghq.com/">homepage of datadog.com</a>
 * 
 * @author man-at-home
 * @since  2015-04-05
 * */
@Configuration
public class DataDogReporterInitializer implements InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(DataDogReporterInitializer.class);

    @Autowired private MetricRegistry metricRegistry; 
    @Autowired private Environment    env; 
    
    public boolean isInitialized = false;

    /** InitalizingBean, will be called on startup. */
    @Override
    public void afterPropertiesSet() throws Exception {
        initDataDogReporter();
    }
    
    /** hook dataDog (statsd) reporter into dropWizard metric registry. */
    public void initDataDogReporter() {
        
        LOG.debug("DataDogReporter initializing for metrics reporting");

        List<String> tags = new ArrayList<String>();
        tags.add("totask2");
        
        String statsdHost = env.getProperty(Totask2Constants.ENV_OPENSHIFT_DATADOG_IP, "localhost");
        int    statsdPort = Integer.parseInt(env.getProperty(Totask2Constants.ENV_OPENSHIFT_DATADOG_DOGSTATSD_PORT, "8125"));
        
        EnumSet<Expansion> expansions = EnumSet.of(Expansion.COUNT, Expansion.RATE_1_MINUTE, Expansion.RATE_15_MINUTE, 
                                                   Expansion.MEDIAN, Expansion.P95, Expansion.P99);
        
        try (Timer.Context context = metricRegistry.timer("TOTASK2XX.datadog.reporter.init.timed").time()) {
                            
            UdpTransport udpTransport = new UdpTransport.Builder()
                    .withStatsdHost(statsdHost)
                    .withPort(statsdPort)
                    .build();
            
            ScheduledReporter reporter = DatadogReporter.forRegistry(AAssert.checkNotNull(metricRegistry))
                      .withTransport(udpTransport)
                      .withExpansions(expansions)
                      .build();
    
             reporter.start(10, TimeUnit.SECONDS);      
             this.isInitialized = true;         
         }
        
         metricRegistry.counter("TOTASK2XX.datadog.reporter.init.count").inc();         
         LOG.info("metrics reporting will go to " + statsdHost + ":" + statsdPort + " via udpTransport");
         return;
    }
    
}
