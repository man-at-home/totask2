package org.regele.totask2.util;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

/** testing duration conversion helper (float parsing). */
public class DurationConverterTest {

    
    @Test
    public void testSimpleIntegerParse() throws Exception {
       assertEquals("parsing 1" ,  new BigDecimal("1.0"),  DurationConverter.parse("1"));
       assertEquals("parsing 2" ,new BigDecimal("2.0"),  DurationConverter.parse("2"));
       assertEquals("parsing 12",new BigDecimal("12.0"),  DurationConverter.parse("12"));
       assertEquals("parsing 0",  new BigDecimal("0.0"),  DurationConverter.parse("0"));
    }

    @Test
    public void testFractionParse() throws Exception {

       assertEquals("parsing 2.2" , new BigDecimal("3.0"),  DurationConverter.parse("2.99"));
        
       assertEquals("parsing 1.1" , new BigDecimal("1.1"),  DurationConverter.parse("1.1"));
       assertEquals("parsing 12.5",new BigDecimal("12.5"),  DurationConverter.parse("12.5"));
       assertEquals("parsing 0.4",  new BigDecimal("0.4"),  DurationConverter.parse("0.4"));
       assertEquals("parsing 0.9",  new BigDecimal("0.9"),  DurationConverter.parse("0.9"));
    }

    @Test
    public void testRoundingParse() throws Exception {
        
       assertEquals("parsing 1.11" , new BigDecimal("1.1"),  DurationConverter.parse("1.11"));
       assertEquals("parsing 2.28" , new BigDecimal("2.3"),  DurationConverter.parse("2.28"));
       assertEquals("parsing 0.99",  new BigDecimal("1.0"),  DurationConverter.parse("0.99"));
       assertEquals("parsing 1.01",  new BigDecimal("1.0"),  DurationConverter.parse("1.01"));
       assertEquals("parsing 2.55",  new BigDecimal("2.6"),  DurationConverter.parse("2.55"));
       assertEquals("parsing 2.54",  new BigDecimal("2.5"),  DurationConverter.parse("2.54"));
    }


    @Test(expected=java.text.ParseException.class)
    public void testMaxLimits() throws Exception {
        
       DurationConverter.parse("25");
    }    
    
    @Test(expected=java.text.ParseException.class)
    public void testMinLimits() throws Exception {
        
       DurationConverter.parse("-1");
    }    

}
