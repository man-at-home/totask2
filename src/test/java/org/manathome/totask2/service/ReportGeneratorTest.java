package org.manathome.totask2.service;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.manathome.totask2.Application;
import org.manathome.totask2.service.ReportGenerator;
import org.manathome.totask2.service.ReportGenerator.ReportOutputFormat;
import org.manathome.totask2.util.SampleData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.ModelAndView;

import com.wordnik.swagger.config.SwaggerConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/** testing jasper report generation. */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class, SwaggerConfig.class })
@WebAppConfiguration
public class ReportGeneratorTest {

    private static final Logger LOG = LoggerFactory
            .getLogger(ReportGeneratorTest.class);
    
    @Autowired
    private ReportGenerator reportGenerator;
    
    private static final String reportName = "reportGeneratorTestReport.jrxml";
    
    private static List<SampleData> reportData = null;
    
    @BeforeClass
    public static void createTestData() {       
        reportData = new ArrayList<SampleData>();
        reportData.add(new SampleData());
        reportData.add(new SampleData());
    }

    /** generate one jasper report as pdf AND excel here. */
    @Test
    public void testRender() throws Exception {

        File pdfReportResult = File.createTempFile("totask2.reportGeneratorTestReport.junit.output.", ".pdf");
        File xlsReportResult = File.createTempFile("totask2.reportGeneratorTestReport.junit.output.", ".xls");
        
        ReportGenerator rg = new ReportGenerator();
       
        try (FileOutputStream fileOutputStream = new FileOutputStream(pdfReportResult, false)) {

            LOG.debug("creating test report: " + pdfReportResult.getAbsolutePath());       

            rg.render(reportName, ReportOutputFormat.pdf, reportData, fileOutputStream);
            assertNotNull("output not generated", fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();
        }
        
        
        try (FileOutputStream fileOutputStream = new FileOutputStream(xlsReportResult, false)) {

            LOG.debug("creating test report: " + pdfReportResult.getAbsolutePath());       

            rg.render(reportName, ReportOutputFormat.excel, reportData, fileOutputStream);
            assertNotNull("output not generated", fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();
        }
 

        assertTrue("output pdf report file " + pdfReportResult.getName() + " does not exist ", pdfReportResult.exists());
        assertTrue("output pdf report with no content + " + pdfReportResult.length(), pdfReportResult.length() > 100);
        
        String content = new String(Files.readAllBytes(Paths.get(pdfReportResult.getAbsolutePath())), Charset.forName("UTF-8"));
        assertThat("pdf content no matchtext", content, CoreMatchers.containsString("%PDF"));
        assertThat("pdf content no matchtext eof", content, CoreMatchers.containsString("%%EOF"));

        assertTrue("output excel report with no content + " + xlsReportResult.length(), xlsReportResult.length() > 100);
        content = new String(Files.readAllBytes(Paths.get(xlsReportResult.getAbsolutePath())), Charset.forName("UTF-8"));
        for (SampleData d : reportData) {
            assertThat("xls report has no matchtext age:" + d.getAge(), content, CoreMatchers.containsString(d.getAge().toString()));            
        }
        
        xlsReportResult.deleteOnExit();
        pdfReportResult.deleteOnExit();

    }

    /** generate one jasper report as pdf AND excel here. */
    @Test
    public void testRenderViews() throws Exception {
    
        ModelAndView mv = reportGenerator.createReportModelView(reportName, ReportOutputFormat.pdf,  reportData);
        assertNotNull(mv);
        assertNotNull(mv.getView());        
    }

}
