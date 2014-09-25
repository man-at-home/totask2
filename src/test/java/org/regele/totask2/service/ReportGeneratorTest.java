package org.regele.totask2.service;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** testing jasper report generation. */
public class ReportGeneratorTest {

    private static final Logger LOG = LoggerFactory
            .getLogger(ReportGeneratorTest.class);

    public class SampleData {
        public String getName() {
            return "my name";
        }

        public String getAge() {
            return " " + Math.random();
        }
    }

    /** generate one jasper report as excel. */
    @Test
    public void testRender() throws Exception {

        File file = File.createTempFile("japser-report.junit", ".pdf");
        LOG.debug("creating test report: " + file.getAbsolutePath());       

        try (FileOutputStream fop = new FileOutputStream(file, false)) {

            List<SampleData> l = new ArrayList<SampleData>();
            l.add(new SampleData());
            l.add(new SampleData());
            
            ReportGenerator rg = new ReportGenerator();

            rg.render(l, fop);

            assertNotNull("output not generated", fop);

            fop.flush();
            fop.close();
        }

        assertTrue("output report file " + file.getName() + " does not exist ", file.exists());
        assertTrue("output report with no content + " + file.length(), file.length() > 100);
        
        String content = new String(Files.readAllBytes( Paths.get( file.getAbsolutePath() )));
        assertThat("pdf content no matchtext", content, CoreMatchers.containsString( "%PDF" ));
        assertThat("pdf content no matchtext eof", content, CoreMatchers.containsString( "%%EOF" ));
        
        file.delete();
    }

}
