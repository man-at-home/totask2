package org.manathome.totask2.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.manathome.totask2.util.LocalDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;


/**
 * exploring new date time classes java 8.
 * 
 * @author man-at-home
 * @since  2014-08-14
 */
public class DateHandlingTest {
    
    /** logging. */
    private static final Logger LOG = LoggerFactory.getLogger(DateHandlingTest.class);

    @Test
    public void testDateParsing() {
        LocalDate ref = LocalDate.of(2014, Month.AUGUST, 14);        
        LocalDate dt = LocalDate.parse("14.08.2014", DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
        LOG.debug(ref + " vs. " + dt);
        assertTrue("correct date parsed " + ref , dt.compareTo(ref) == 0);
    }
    
    @Test
    public void testDateManipulation() {
        LocalDate ref = LocalDate.of(2014, Month.AUGUST, 14);        
        LocalDate dt =  ref.with(TemporalAdjusters.lastDayOfMonth());
        assertTrue("correct last of month" ,  dt.getDayOfMonth() == 31 && dt.getMonthValue() == 8);
        
        dt = dt.minusDays(2);
        assertTrue("2 days before end of august", dt.getDayOfMonth() == 29);
    }    

    @Test
    public void testISODateParsing() {
        LocalDate ref = LocalDate.of(2014, Month.AUGUST, 14);        
        LocalDate dt = LocalDate.parse("2014-08-14");
        assertTrue("correct date parsed " + ref , dt.compareTo(ref) == 0);
    }
    
    @Test
    public void testLocalDateConversion() {
        LocalDate ref = LocalDate.of(2014, Month.AUGUST, 14);  
        Date d = LocalDateConverter.toDate(ref);
        
        LocalDate ld = LocalDateConverter.toLocalDate(new Date(d.getTime()));       
        assertEquals("conversion ok", ref.toEpochDay(), ld.toEpochDay());
        
        ref = LocalDate.now();
        assertTrue("today before now",   new Date().after(LocalDateConverter.toDate(ref)));
        assertTrue("tomorrow after now", new Date().before(LocalDateConverter.toDate(ref.plusDays(1))));       
    }    

    @Test(expected = DateTimeParseException.class)
    public void testInvalidParsing() {
        LocalDate dt = LocalDate.parse("x1.2.2011");
        fail("parse exception expected not " + dt);
    }    
    
}
