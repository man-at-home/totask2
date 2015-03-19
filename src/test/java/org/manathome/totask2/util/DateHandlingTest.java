package org.manathome.totask2.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;
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

    /** german parsing of dates. */
    @Test
    public void testValidDateParsing() {
        LocalDate ref = LocalDate.of(2014, Month.AUGUST, 14);        
        LocalDate dt = LocalDateConverter.parse("14.08.2014");
        LOG.debug(ref + " vs. " + dt);
        assertThat("correct date parsed ", dt, is(ref));
        
        ref = LocalDate.of(2015, Month.DECEMBER, 30);        
        dt = LocalDateConverter.parse("30.12.2015");
        LOG.debug(ref + " vs. " + dt);
        assertThat("correct date parsed ", dt, is(ref));

        ref = LocalDate.of(1999, Month.JANUARY, 1);        
        dt = LocalDateConverter.parse("01.01.1999");
        LOG.debug(ref + " vs. " + dt);
        assertThat("correct date parsed ", dt, is(ref));
        
        ref = LocalDate.of(1999, Month.JANUARY, 2);        
        dt = LocalDateConverter.parse("2.1.1999");
        LOG.debug(ref + " vs. " + dt);
        assertThat("correct date parsed ", dt, is(ref));
    }
    
    /** german formatting of dates. */
    @Test
    public void testValidDateFormatting() {

        String ref = "14.8.1911";        
        String dt =  LocalDateConverter.format(LocalDate.of(1911, Month.AUGUST, 14));
        LOG.debug(ref + " vs. " + dt);
        assertThat("correct date formatted ", dt , is(ref));
        
        ref = "14.1.2015";        
        dt =  LocalDateConverter.format(LocalDate.of(2015, Month.JANUARY, 14));
        LOG.debug(ref + " vs. " + dt);
        assertThat("correct date formatted ", dt , is(ref));

        ref = "1.11.2015";        
        dt =  LocalDateConverter.format(LocalDate.of(2015, Month.NOVEMBER, 1));
        LOG.debug(ref + " vs. " + dt);
        assertThat("correct date formatted ", dt , is(ref));
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
        LocalDate dt = LocalDateConverter.parse("x1.2.2011");
        fail("parse exception expected not " + dt);
    }    
    
}
