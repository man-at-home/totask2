package org.regele.totask2.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;

import org.regele.totask2.util.EnvironmentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;


/**
 * report generation with jasper report.
 * 
 * @author manfred
 * */
public class ReportGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(ReportGenerator.class);

    public void render(final Collection<?> pojoDataSource, OutputStream outputStream) throws EnvironmentException {
        try {
            String reportName = "/reports/emptyReport.jrxml";
            
            HashMap<String, Object> reportParams = new HashMap<String, Object>();
            reportParams.put("reportTitle", "manfred User Report");

            // 2. Retrieve template
            LOG.debug("loading report..: " + this.getClass().getResource(reportName));
            InputStream reportStream = this.getClass().getResourceAsStream(reportName);

            // 3. Convert template to JasperDesign
            JasperDesign jd = JRXmlLoader.load(reportStream);
            LOG.debug("compiling report: " + jd.getName());

            // 4. Compile design to JasperReport
            JasperReport jr = JasperCompileManager.compileReport(jd);
            LOG.debug("filling report..: " + jr.getName() + " with " + pojoDataSource.size() + " rows.");    
            
            // 5. Create the JasperPrint object
            // Make sure to pass the JasperReport, report parameters, and data
            // source
            JasperPrint jp = JasperFillManager.fillReport(jr, reportParams, buildDataSource(pojoDataSource));
            
            LOG.debug("exporting report.: " + jp.getName() );    
            
            JasperExportManager.exportReportToPdfStream(jp, outputStream);
 
      /*      JRXlsExporter exporter = new JRXlsExporter();
            
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jp));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
            
            exporter.exportReport();
      */
        } catch (JRException jrex) {
            LOG.error("Error render Report", jrex);
            throw new EnvironmentException("Error generation Report: "
                    + jrex.getMessage(), jrex);
        }
    }

    /**
     * Returns a data source that's wrapped within {@link JRDataSource}
     * 
     * @return
     */
    private JRDataSource buildDataSource(Collection<?> data) {
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(data, true);
        LOG.debug("  DataSource[" + ds.getRecordCount() + "]");
        return ds;
    }

}
