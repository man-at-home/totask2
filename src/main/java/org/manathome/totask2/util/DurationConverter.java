package org.manathome.totask2.util;


import org.manathome.totask2.model.WorkEntry;

import java.math.BigDecimal;
import java.text.ParseException;

/** 
 * helper for duration conversion. 
 * 
 * @see    WorkEntry#getDuration()
 * @author man-at-home
 * */
public final class DurationConverter {
    
    /** parse duration from text. 
     * 
     * @throws ParseException
     * @throws InvalidDurationException
     * */
    public static BigDecimal parse(final String durationString) throws ParseException {
        
        if (durationString == null || durationString.length() == 0) {
            return new BigDecimal("0");
        }
        
        BigDecimal duration = new BigDecimal(durationString);   
        if (duration.doubleValue() < 0) {
            throw new InvalidDurationException("duration must be positive, not " + durationString);
        } else if (duration.doubleValue() > 24) {
            throw new InvalidDurationException("duration must be within a day (24h), not " + durationString);
        } else {
            duration = duration.setScale(1, BigDecimal.ROUND_HALF_UP);
            return duration;
        }
    }

    /** duration.toString() or "". */
    public static String format(final BigDecimal duration) {
          return duration == null ? "" : duration.toString();
    }  

}
