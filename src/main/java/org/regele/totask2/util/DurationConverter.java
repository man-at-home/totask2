package org.regele.totask2.util;

import java.math.BigDecimal;
import java.text.ParseException;



/** helper for duration conversion. */
public final class DurationConverter {
    
    //private static final Logger LOG = LoggerFactory.getLogger(DurationConverter.class);

    /** needs work. */
    public static BigDecimal parse(final String durationString) throws ParseException {
        
        BigDecimal duration = new BigDecimal(durationString);   
        if( duration.doubleValue() < 0)
            throw new ParseException( "duration must be positive, not " + durationString, 0);
        else if( duration.doubleValue() > 24)
            throw new ParseException( "duration must be within a day (24h), not " + durationString, 0);
        else {
            duration = duration.setScale(1, BigDecimal.ROUND_HALF_UP);
            return duration;
        }
    }

    /** dummy. */
    public static String format(final BigDecimal duration) {
          return duration.toString();
    }  

}
